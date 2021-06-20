package io.iohk.apidoc
package jsonschema

import agnostic._

sealed trait Element

/**
  * Represents a sealed trait hierarchy
  *
  * @param allOf
  */
case class AllOfElement( allOf: Seq[Element] ) extends Element

/**
  * Represents a product (case class)
  *
  * @param 
  */
case class RefElement( `$ref`: String ) extends Element

/**
  * Represents a value (string / integer / boolean)
  *
  * @param type
  */
case class ValElement( `type`: String ) extends Element


/**
  * Represents a typed array
  *
  * @param type - fixed for "array" keyword. (TODO: create a private constructor)
  * @param items - element representation
  */
case class ArrElement( `type`: String = "array", items: Element ) extends Element


object Element {

  def from(ap: Parameter): Element = {
    ap match {
      case ProductParameter(name, _) => RefElement(s"#/components/schemas/$name")
      case CoproductParameter(els) => AllOfElement( els.map(Element.from) )
      case SeqParameter(el) => ArrElement(items = Element.from(el))
      case OptionalParameter(inner) => Element.from(inner)
      case ValueParameter(tpe) => ValElement(tpe)
    }
  }
}
