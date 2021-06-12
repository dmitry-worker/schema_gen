package gen

import language.experimental.macros, magnolia._

object Repr { 

  type Typeclass[T] = Schema.Repr[T]

  def combine[T](ctx: CaseClass[Schema.Repr, T]): Schema.Repr[T] = new Schema.Repr[T] {
    def apply: Schema.Param = {
      Schema.Param(
        name = "result",
        description = None,
        required = None,
        schema = Schema.ObjParam(
          required = Nil,
          properties = ctx.parameters.map { p =>
            p.typeclass.apply.schema
          } 
        )
      )
     
    }
  }

  implicit def optParam[T](implicit inner: Schema.Repr[T]): Schema.Repr[Option[T]] = new Schema.Repr[Option[T]] {
    def apply: Schema.Param = inner.apply.copy(required = None)
  }

  implicit val paramInt: Schema.Repr[Int] = new Schema.Repr[Int] {
    def apply: Schema.Param = Schema.Param(
      name = "One",
      description = None,
      required = Some(true),
      schema = Schema.ValParam("integer")
    )
  }

  implicit val paramString: Schema.Repr[String] = new Schema.Repr[String] {
    def apply: Schema.Param = Schema.Param(
      name = "Two",
      description = None,
      required = Some(true),
      schema = Schema.ValParam("string")
    )
  }

  implicit def gen[T]: Schema.Repr[T] = macro Magnolia.gen[T]

}