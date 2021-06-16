package gen

object JsonSchema {

  case class Defn( 
    `type`: String,
    required: Seq[String],
    properties: Map[String, Param]
  )
  
  sealed trait Param
  case class CoproductDefn( allOf: Seq[Param] ) extends Param
  case class RefParam( `$ref`: String ) extends Param
  case class ValParam( `type`: String ) extends Param
  case class ArrParam( `type`: String = "array", items: Param ) extends Param

  object Param {

    def from(ap: Agnostic.Parameter): Param = {
      ap match {
        case Agnostic.ProductParameter(name, _) => RefParam(s"#/components/schemas/$name")
        case Agnostic.CoproductParameter(els) => CoproductDefn( els.map(Param.from) )
        case Agnostic.SeqParameter(el) => ArrParam(items = Param.from(el))
        case Agnostic.OptionalParameter(inner) => Param.from(inner)
        case Agnostic.ValueParameter(tpe) => ValParam(tpe)
      }
    }
  }

} 