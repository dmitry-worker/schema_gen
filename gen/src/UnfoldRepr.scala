package gen

import language.experimental.macros, magnolia._

// object UnfoldRepr { 

//   type Typeclass[T] = Schema.UnfoldRepr[T]

//   def combine[T](ctx: CaseClass[Schema.Repr, T])(implicit s: Schema.Repr[T]): Schema.UnfoldRepr[T] = {
//     new Schema.UnfoldRepr[T] {
//       def apply(value: T): Seq[Schema.Param] = ctx.parameters.map { p =>
//         val (descr, exampel) = p.annotations.collectFirst({ 
//           case RPCParam(description, example) => (description, example)
//         }).getOrElse(None, None)
//         Schema.Param(
//           name = p.label,
//           description = descr,
//           required = Some(true),
//           schema = Schema.ValParam(p.typeName.short)
//         )
//       }
//     }
//   }

//   implicit def genUnfold[T]: Schema.Repr[T] = macro Magnolia.gen[T]

// }