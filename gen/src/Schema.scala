package gen

import Schema._

final case class RPCParam(
  description: Option[String], 
  example: Option[String]
) extends scala.annotation.StaticAnnotation

final case class Schema(
  openrpc: String = "1.0.0-rc1",
  info: SchemaInfo,
  servers: List[ServerInfo],
  methods: List[MethodInfo],
  components: Components
) {

  def method[Req: Schema.Repr, Resp: Schema.Repr](
    name: String, 
    description: Option[String]
  ) = {

    val paramRoot = implicitly[Repr[Req]].apply
    val params = paramRoot.schema match { 
      case ObjParam(_, props) => props
      case ArrParam(_, items) => items
      case x => x :: Nil
    }

    MethodInfo(
      name,
      description,
      ???, //params, 
      implicitly[Repr[Resp]].apply.schema
    )
  }

  def dummyMethod(method: MethodInfo): Schema = {
    copy(methods = method :: methods)
  }

}

object Schema {

  trait Repr[A] {
    def apply: Param
  }

  trait UnfoldRepr[A] {
    def apply: Seq[Param]
  }

  case class Components(
    schemas: Map[String, ObjParam]
  )

  case class ServerInfo(
    url: String
  )

  case class SchemaInfo(
    version: String,
    title: String,
    description: String,
    termsOfService: Option[String],
    contact: Option[SchemaInfo.Contact],
    license: Option[SchemaInfo.License]
  )

  object SchemaInfo {
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
  
  final case class MethodInfo(
    name: String,
    summary: Option[String],
    params: Seq[Param],
    result: ParamValue
  )

  final case class Param( 
    name: String,
    description: Option[String],
    required: Option[Boolean],
    schema: ParamValue
  )

  // element
  sealed trait ParamValue
  sealed trait SingleParamValue extends ParamValue
  final case class RefParam(`$ref`: String) extends SingleParamValue
  final case class ValParam(`type`: String) extends SingleParamValue
  final case class ArrParam(`type`: String, items: SingleParamValue) extends ParamValue
  final case class ObjParam(
    required: Seq[String],
    properties: Seq[ParamValue]
  ) extends ParamValue

}