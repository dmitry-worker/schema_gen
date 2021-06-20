package io.iohk.apidoc
package openrpc

import io.circe.syntax._
import io.circe.{Json, Encoder}
import io.circe.generic.auto._

/**
  * OpenRPC standard definition is two-fold
  * First is self-defined methods and parameters https://spec.open-rpc.org
  * Second is JsonSchema https://json-schema.org/specification.html
  *
  * @param openrpc - version of OpenRPC specification
  * @param info - Schema meta-information
  * @param servers - Urls supporting current schema
  * @param methods - RPC methods (Request + Response definition)
  * @param components - Types reference (Schema domain model)
  */
final case class Schema(
  openrpc: String = "1.0.0-rc1",
  info: Info = Info.empty,
  servers: Seq[Schema.ServerInfo] = Nil,
  methods: Seq[Method],
  components: Schema.Components
) {

  /**
    * Sets openrpc protocol version
    * @param newVersion New version of RPC specification
    * @return new instance of schema with version redefined
    */
  def withVersion(newVersion: String):Schema = {
    copy(openrpc = newVersion)
  }

  /**
    * Sets meta information for JsonSchema
    * @param newInfo New schema information
    * @return new instance of schema with info redefined
    */
  def withInfo(newInfo: Info):Schema = {
    copy(info = newInfo)
  }

  /**
    * Sets the servers with this schema deployed
    * @param newServers
    * @return new instance of schema with servers redefined
    */
  def withServers(newServers: Seq[Schema.ServerInfo]):Schema = {
    copy(servers = newServers)
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
    *  Complete OpenRPC schema json representation
    */
  lazy val jsonValue:Json = {
    this.asJson.deepDropNullValues
  }

}

object Schema {

  /**
    * Contains all the JsonSchema definitions 
    * That are mentioned in parameters AST
    * 
    * This cannot  
    * @param schemas - list of definitions
    */
  final case class Components(
    schemas: Map[String, jsonschema.TypeDefinition]
  )

  /**
    * Contains list of endpoints supporting this schema.
    * @param url - the endpoint address.
    */
  final case class ServerInfo(
    url: String
  )

  def from(src: agnostic.Schema): Schema = {
    val components = {
      val allParameters = src.methods.flatMap(_.allParameters)
      Method.collectAllTypes(Map(), allParameters)
    }

    Schema(      
      methods = src.methods.map(Method.from),
      components = Components(components)
    )
  }

}
