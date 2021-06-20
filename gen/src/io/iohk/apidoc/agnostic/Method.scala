package io.iohk.apidoc
package agnostic

final case class Method(
  name: String,
  summary: Option[String],
  params: Seq[ProductParameterField],
  result: ProductParameterField
) { 
  lazy val allParameters:List[Parameter] = 
    (result :: params.toList).map(_.schema)
}