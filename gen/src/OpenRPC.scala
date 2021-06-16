package gen
import gen.Agnostic.CoproductParameter
import cats.instances.tailRec
import scala.annotation.tailrec
import gen.Agnostic.OptionalParameter
import io.circe.syntax._
import io.circe.{Json, Encoder}
import io.circe.generic.auto._


object OpenRpc {

  def createSchema(src: Agnostic.Schema) = {

    val allParameters = src.methods.flatMap(_.allParameters)
    val components = Method.collect0(Map(), allParameters)

    Schema(
      info = OpenRpc.Info(
        version = "1.0.0",
        title = "Test schema",
        description = "About to test and validate schema",
        termsOfService = None,
        contact = None,
        license = None
      ),
      servers = Nil,
      methods = src.methods.map(Method.from),
      components = Components(components)
    )
  }

  case class Schema(
    openrpc: String = "1.0.0-rc1",
    info: Info,
    servers: Seq[ServerInfo],
    methods: Seq[Method],
    components: Components
  ) {

    private implicit lazy val encodeParam: Encoder[JsonSchema.Param] = Encoder.instance {
      case bar @ JsonSchema.RefParam(_) => bar.asJson
      case baz @ JsonSchema.ValParam(_) => baz.asJson
      case qux @ JsonSchema.ArrParam(_, _) => qux.asJson
      case foo @ JsonSchema.CoproductDefn(_) => foo.asJson
    }

    lazy val jsonValue:Json = {
      this.asJson.deepDropNullValues
    }

   }

  case class Method(
    name: String,
    summary: Option[String],
    params: Seq[ParamDescriptor],
    result: ParamDescriptor
  )

  object Method {

    // @tailrec
    def collect0(acc: Map[String, JsonSchema.Defn], parameters: Seq[Agnostic.Parameter]): Map[String, JsonSchema.Defn] = {
      
      @tailrec
      def collect1(acc: Map[String, JsonSchema.Defn], parameter: Agnostic.Parameter): Map[String, JsonSchema.Defn] = {
        parameter match {
          case Agnostic.ProductParameter(name, fields) =>
            val _req: List[String] = Nil
            val _properties: Map[String, JsonSchema.Param] = Map()
            val (requiredFields, properties) = fields.foldLeft(_req -> _properties) { 
              case ((required, props), el) => 
                val param = JsonSchema.Param.from(el.schema)
                val newProps = props + (el.name -> param)
                val newReq = el.schema match { 
                  case Agnostic.OptionalParameter(_) => required
                  case _ => el.name :: required
                }
                (newReq, newProps)
            }
            val current = JsonSchema.Defn(
              `type` = "object",
              required = requiredFields,
              properties = properties
            )
            collect0(acc + (name -> current), fields.map(_.schema))
          case Agnostic.CoproductParameter(elements) => collect0(acc, elements)
          case Agnostic.OptionalParameter(inner) => collect1(acc, inner)
          case Agnostic.SeqParameter(inner) => collect1(acc, inner)
          case Agnostic.ValueParameter(tpe) => acc
        }
      }

      parameters.foldLeft(acc) { (r, el) => collect1(r, el) }

    }

    def from(src: Agnostic.MethodInfo) = { 
      Method(
        name = src.name,
        summary = src.summary,
        params = src.params.map(ParamDescriptor.from),
        result = ParamDescriptor.from(src.result)
      )
    }

  }

  case class Info(
    version: String,
    title: String,
    description: String,
    termsOfService: Option[String],
    contact: Option[Info.Contact],
    license: Option[Info.License]
  )

  object Info {
    case class Contact(
      name: String,
      email: String,
      url: Option[String]
    )
    case class License(
      name: String,
      url: Option[String]
    )
  }

  case class Components(
    schemas: Map[String, JsonSchema.Defn]
  )

  case class ServerInfo(
    url: String
  )

  case class ParamDescriptor(
    name: String,
    description: Option[String],
    schema: JsonSchema.Param
  )

  object ParamDescriptor {

    def from(src: Agnostic.ProductParameterField): ParamDescriptor = {
      ParamDescriptor(
        name = src.name,
        description = src.description,
        schema = JsonSchema.Param.from(src.schema)
      )
    }

  }

  // sealed trait Param
  // case class RefParam( `$ref`: String ) extends Param
  // case class ValParam( `type`: String ) extends Param
  // case class ArrParam( `type`: String = "array", items: Param ) extends Param

  // object Param { 
  //   def from(src: Agnostic.Parameter): Param = ???
  // }

}