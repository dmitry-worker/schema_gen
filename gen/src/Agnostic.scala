package gen

final case class RPCParam(
  description: Option[String], 
  example: Option[String]
) extends scala.annotation.StaticAnnotation


object Agnostic {

  final case class Schema(
    methods: List[MethodInfo] = Nil,
  ) {

    def withMethod(method: MethodInfo): Schema = {
      copy(methods = method :: methods)
    }

    def method[Req: Repr, Resp: Repr](
      name: String, 
      description: Option[String]
    ): Schema = {
      
      val paramsObject = implicitly[Repr[Req]].apply
      val parameters = paramsObject match {
        case ProductParameter(_, properties) => properties
        case OptionalParameter(ProductParameter(_, properties)) => properties
        case _ => throw new IllegalArgumentException("Only case class / option is allowed")
      }

      val resultObject = implicitly[Repr[Resp]].apply
      val result = resultObject match {
        case ProductParameter(label, properties) if properties.size == 1 => 
          // First argument is our response
          properties.head
        case _ => 
          // Probably the object itself is a response
          ProductParameterField("result", None, resultObject)
      }

      withMethod(MethodInfo(
        name,
        description,
        parameters,
        result
      ))

    }

  }

  trait Repr[A] {
    def apply: Parameter
  }
  
  final case class MethodInfo(
    name: String,
    summary: Option[String],
    params: Seq[ProductParameterField],
    result: ProductParameterField
  ) { 
    def allParameters:List[Parameter] = (result :: params.toList).map(_.schema)
  }

  final case class ProductParameterField( 
    name: String,
    description: Option[String],
    schema: Parameter
  )


  // element
  sealed trait Parameter

  final case class ProductParameter(
    label: String, 
    properties: Seq[ProductParameterField]
  ) extends Parameter

  final case class CoproductParameter(
    elements: Seq[Parameter]
  ) extends Parameter

  final case class SeqParameter(
    schema: Parameter
  ) extends Parameter

  final case class ValueParameter(
    `type`: String
  ) extends Parameter

  final case class OptionalParameter(
    inner: Parameter
  ) extends Parameter

}