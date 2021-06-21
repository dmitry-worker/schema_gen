package io.iohk.apidoc
package agnostic

import language.experimental.macros, magnolia._
import Annotations._

object SchemaMacros { 

  type Typeclass[T] = Repr[T]
  
  def combine[T](ctx: CaseClass[Repr, T]): Repr[T] = new Repr[T] {
    def apply: Parameter = {
      ProductParameter(
        label = ctx.typeName.short,
        properties = ctx.parameters.map { p =>
          val description = p.annotations.collectFirst { 
              case FieldDescription(value) => value
          }
          val schema = p.typeclass.apply
          ProductParameterField(
            name = p.label,
            description = description,
            schema = schema
          )
        } 
      )
    }
  }
  
  def dispatch[T](ctx: SealedTrait[Repr, T]): Repr[T] = new Repr[T] {
    def apply: Parameter = {
      val elements = ctx.subtypes.map(_.typeclass.apply)
      CoproductParameter(elements)
    }
  }
  
  implicit def optParam[T](implicit inner: Repr[T]): Repr[Option[T]] = new Repr[Option[T]] {
    def apply: Parameter = OptionalParameter(inner.apply)
  }
  
  implicit val intParam: Repr[Int] = new Repr[Int] {
    def apply: Parameter = ValueParameter(
      `type` = "integer"
    )
  }

  implicit val numberParam: Repr[Double] = new Repr[Double] {
    def apply: Parameter = ValueParameter(
      `type` = "number"
    )
  }
  
  implicit val stringParam: Repr[String] = new Repr[String] {
    def apply: Parameter = ValueParameter(
      `type` = "string"
    )
  }

  implicit val booleanParam: Repr[Boolean] = new Repr[Boolean] {
    def apply: Parameter = ValueParameter(
      `type` = "boolean"
    )
  }

  implicit def gen[T]: Repr[T] = macro Magnolia.gen[T]

}