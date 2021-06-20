package io.iohk.apidoc
package openrpc

import io.circe.syntax._
import io.circe.{Json, Encoder}
import io.circe.generic.auto._

case class Schema(
  openrpc: String = "1.0.0-rc1",
  info: Info,
  servers: Seq[Schema.ServerInfo] = Nil,
  methods: Seq[Method] = Nil,
  components: Schema.Components = Schema.Components(Map())
) {

  // this is required for circe to encode only inner members of the CoProduct 
  // Without wrapping it to object with a type signature.
  private implicit lazy val encodeParam: Encoder[jsonschema.Element] = Encoder.instance {
    case reference:jsonschema.RefElement => reference.asJson
    case value:jsonschema.ValElement => value.asJson
    case array:jsonschema.ArrElement => array.asJson
    case allOf:jsonschema.AllOfElement => allOf.asJson
  }

  /**
    * @return Complete schema for publishing in the application
    */
  lazy val jsonValue:Json = {
    this.asJson.deepDropNullValues
  }

}

object Schema {

  case class Components(
    schemas: Map[String, jsonschema.TypeDefinition]
  )

  case class ServerInfo(
    url: String
  )

}
