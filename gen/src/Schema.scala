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
    ???
    // MethodInfo(
    //   name,
    //   description,
    //   implicitly[Repr[Req]].apply,
    //   implicitly[Repr[Resp]].apply.head.schema
    // )
  }

  def dummyMethod(method: MethodInfo): Schema = {
    copy(methods = method :: methods)
  }

}

object Schema {

  trait Repr[A] {
    def apply: Parameter
  }

  case class Components(
    schemas: Map[String, ProductParameter]
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
    params: Seq[ProductParameterField],
    result: Parameter
  )

  final case class ProductParameterField( 
    name: String,
    description: Option[String],
    required: Option[Boolean],
    schema: Parameter
  )

  // element
  sealed trait Parameter
  sealed trait SingularParameter extends Parameter

  final case class ProductParameter(
    properties: Seq[ProductParameterField]
  ) extends SingularParameter

  final case class CoproductParameter(
    elements: Seq[Parameter]
  ) extends SingularParameter

  final case class SeqParameter(
    schema: SingularParameter
  ) extends Parameter

  final case class ValueParameter(
    `type`: String
  ) extends SingularParameter


}