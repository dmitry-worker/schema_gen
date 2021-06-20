package io.iohk.apidoc.jsonschema


/**
  * Represents the root element of the JsonSchema
  * Typically a case class representation
  *
  * @param type - short name of the class
  * @param required - names of all fields that are mandatory
  * @param properties - properties definition
  */
final case class TypeDefinition(
  `type`: String,
  required: Seq[String],
  properties: Map[String, Element]
)