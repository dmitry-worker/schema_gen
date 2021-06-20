package io.iohk.apidoc
package agnostic


trait Repr[A] {
  def apply: Parameter
}

sealed trait Parameter

final case class ProductParameter(
  label: String, 
  properties: Seq[ProductParameterField]
) extends Parameter

final case class ProductParameterField( 
  name: String,
  description: Option[String],
  schema: Parameter
)

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