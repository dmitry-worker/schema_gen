package io.iohk.apidoc.agnostic

/**
  * Type / field annotations can be used to fill in various properties
  * Such as field description and documentation.
  */
object Annotations {

  final case class FieldDescription(
    description: String, 
  ) extends scala.annotation.StaticAnnotation
  
}
