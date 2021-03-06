package io.iohk.apidoc
package agnostic


/**
  * Agnostic schema is not bound to any implementation (JsonSchema / OpenRPC / etc)
  * It has all information about types involved in schema 
  * And is used to generate both (JsonSchema, OpenRPC, ...) 
  */
final case class Schema(
  methods: List[Method] = Nil,
) {

  /**
    * Add a new method to schema
    *
    * @param name - method name 
    * @param description - method description (short)
    * @return
    */
  def method[Req: Repr, Resp: Repr](
    name: String, 
    description: Option[String]
  ): Schema = {

    // Create a representation schema for the result type
    // Assuming that result is always a single object
    val resultObject = implicitly[Repr[Resp]].apply

    // Create a representation schema for the parameters container
    // Assuming it's a case class with multiple parameters (flattened in the request)
    val paramsObject = implicitly[Repr[Req]].apply
    
    val parameters = paramsObject match {
      case ProductParameter(_, properties) => properties
      case OptionalParameter(ProductParameter(_, properties)) => properties
      case _ => throw new IllegalArgumentException("Only case class / option is allowed")
    }
    
    val result = resultObject match {
      case ProductParameter(label, properties) if properties.size == 1 => 
        // First argument is our response
        properties.head
      case _ => 
        // Probably the object itself is a response
        ProductParameterField("result", None, resultObject)
    }

    val newMethod = Method(name, description, parameters, result)
    copy(methods = newMethod :: this.methods)

  }

}