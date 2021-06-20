package io.iohk.apidoc
package openrpc

case class ParamDescriptor(
  name: String,
  description: Option[String],
  schema: jsonschema.Element
)

object ParamDescriptor {

  def from(src: agnostic.ProductParameterField): ParamDescriptor = {
    ParamDescriptor(
      name = src.name,
      description = src.description,
      schema = jsonschema.Element.from(src.schema)
    )
  }

}