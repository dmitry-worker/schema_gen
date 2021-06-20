package io.iohk.apidoc.openrpc

import cats.instances.tailRec
import scala.annotation.tailrec
import io.circe.syntax._
import io.circe.{Json, Encoder}
import io.circe.generic.auto._


object OpenRpc {

  def createSchema(src: agnostic.Schema) = {

    val allParameters = src.methods.flatMap(_.allParameters)
    val components = Method.collect0(Map(), allParameters)

    Schema(
      info = OpenRpc.Info(
        version = "1.0.0",
        title = "Test schema",
        description = "About to test and validate schema",
        termsOfService = None,
        contact = None,
        license = None
      ),
      servers = Nil,
      methods = src.methods.map(Method.from),
      components = Components(components)
    )
  }

}