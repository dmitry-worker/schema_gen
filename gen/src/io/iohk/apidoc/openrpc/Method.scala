package io.iohk.apidoc
package openrpc
                 
import jsonschema.TypeDefinition
import scala.annotation.tailrec


case class Method(
  name: String,
  summary: Option[String],
  params: Seq[ParamDescriptor],
  result: ParamDescriptor
)

object Method {

  @tailrec
  def collectAllTypes(acc: Map[String, TypeDefinition], parameters: Seq[agnostic.Parameter]): Map[String, TypeDefinition] = {
    parameters.foldLeft(acc) { (r, el) => collectParamTypes(r, el) }
    if (parameters.isEmpty) acc
    else {
      val headFolded = collectParamTypes(acc, parameters.head)
      collectAllTypes(headFolded, parameters.tail)
    }
  }

  @tailrec
  private def collectParamTypes(acc: Map[String, TypeDefinition], parameter: agnostic.Parameter): Map[String, TypeDefinition] = {
    parameter match {
      case agnostic.ProductParameter(name, fields) =>
        val _req: List[String] = Nil
        val _properties: Map[String, jsonschema.Element] = Map()
        val (requiredFields, properties) = fields.foldLeft(_req -> _properties) { 
          case ((required, props), el) => 
            val param = jsonschema.Element.from(el.schema)
            val newProps = props + (el.name -> param)
            val newReq = el.schema match { 
              case agnostic.OptionalParameter(_) => required
              case _ => el.name :: required
            }
            (newReq, newProps)
        }
        val current = TypeDefinition(
          `type` = "object",
          required = requiredFields,
          properties = properties
        )
        collectAllTypes(acc + (name -> current), fields.map(_.schema))
      case agnostic.CoproductParameter(elements) => collectAllTypes(acc, elements)
      case agnostic.OptionalParameter(inner) => collectParamTypes(acc, inner)
      case agnostic.SeqParameter(inner) => collectParamTypes(acc, inner)
      case agnostic.ValueParameter(tpe) => acc
    }
  }

  def from(src: agnostic.Method) = { 
    Method(
      name = src.name,
      summary = src.summary,
      params = src.params.map(ParamDescriptor.from),
      result = ParamDescriptor.from(src.result)
    )
  }

}
