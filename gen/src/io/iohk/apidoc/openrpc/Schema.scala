package io.iohk.apidoc
package openrpc

import io.circe.syntax._
import io.circe.{Json, Encoder}
import io.circe.generic.auto._

/**
  * JsonSchema standard 
  *
  * @param openrpc
  * @param info
  * @param servers
  * @param methods
  * @param components
  */
case class Schema(
  openrpc: String = "1.0.0-rc1",
  info: Info = Info.empty,
  servers: Seq[Schema.ServerInfo] = Nil,
  methods: Seq[Method],
  components: Schema.Components
) {

  /**
    * Sets openrpc protocol version
    * @param newVersion New version of RPC specification
    */
  def withVersion(newVersion: String) = {
    copy(openrpc = newVersion)
  }

  /**
    * Sets meta information for JsonSchema
    * @param newInfo New schema information
    */
  def withInfo(newInfo: Info) = {
    copy(info = newInfo)
  }

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

  def from(src: agnostic.Schema): Schema = {
    val components = {
      val allParameters = src.methods.flatMap(_.allParameters)
      Method.collect0(Map(), allParameters)
    }

    Schema(      
      methods = src.methods.map(Method.from),
      components = Components(components)
    )
  }

}
