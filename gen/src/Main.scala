package gen

import Repr._
import io.circe.syntax._
import io.circe.generic.auto._

case class GetBlockByIndexRequest(i: Int)

case class Block( 
  hash: String,
  index: Int,
  someShit: String
)

case class BlockResponse(block: Block)

object Main {

  def main(args:Array[String]):Unit = {

    implicit val reqGen:Schema.Repr[GetBlockByIndexRequest] = gen[GetBlockByIndexRequest]
    implicit val respGen = gen[BlockResponse]
    val inputParam = Schema.Param("name", Some("desc"), Some(true), Schema.ValParam("string"))
    val outputParam = Schema.Param("result", Some("result desc"), None, Schema.ValParam("intger"))

    val schema = Schema(
      info = Schema.SchemaInfo(
        version = "1.0.0",
        title = "Test schema",
        description = "About to test and validate schema",
        termsOfService = None,
        contact = None,
        license = None
      ),
      components = Schema.Components(Map()),
      servers = Nil,
      methods = Nil
    ).method[GetBlockByIndexRequest, BlockResponse]("testMethod", Some("testMethodDescr"))
    val json = schema.asJson.deepDropNullValues.spaces2
    println(json)

  }
  
}