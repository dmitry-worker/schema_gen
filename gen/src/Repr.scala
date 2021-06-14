package gen

import language.experimental.macros, magnolia._

object Repr { 

  type Typeclass[T] = Schema.Repr[T]

  def combine[T](ctx: CaseClass[Schema.Repr, T]): Schema.Repr[T] = new Schema.Repr[T] {
    def apply: Schema.Parameter = {
      Schema.ProductParameter(
        properties = ctx.parameters.map { p =>
          p.typeclass.apply
        } 
      )
    }
  }

  def dispatch[T](ctx: SealedTrait[Schema.Repr, T]): Schema.Repr[T] = new Schema.Repr[T] {
    def apply: Schema.Parameter = {
      val elements = ctx.subtypes.map { sub => sub.typeclass.apply }
      Schema.CoproductParameter(elements)
    }
  }

  implicit def optParam[T](implicit inner: Schema.Repr[T]): Schema.Repr[Option[T]] = new Schema.Repr[Option[T]] {
    def apply: Schema.Parameter = inner.apply.copy(required = None)
  }

  implicit val paramInt: Schema.Repr[Int] = new Schema.Repr[Int] {
    def apply: Schema.Parameter = Schema.ValueParameter(
      `type` = "integer"
    )
  }

  implicit val paramString: Schema.Repr[String] = new Schema.Repr[String] {
    def apply: Schema.Parameter = Schema.ValueParameter(
      `type` = "string"
    )
  }

  implicit def gen[T]: Schema.Repr[T] = macro Magnolia.gen[T]

}