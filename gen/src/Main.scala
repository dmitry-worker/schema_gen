package gen

import SchemaMacros._
import io.circe.syntax._
import io.circe.Encoder
import io.circe.generic.auto._

case class GetBlockByIndexRequest(i: Int)

case class GetBlockByHashRequest(hash: Int)

case class Block( 
  hash: String,
  index: Int,
  someShit: Option[String]
)

case class BlockResponse(block: Block)

object Main {

  def main(args:Array[String]):Unit = {

    implicit val reqGen:Agnostic.Repr[GetBlockByIndexRequest] = gen[GetBlockByIndexRequest]
    implicit val respGen = gen[BlockResponse]
    val inputParam = Agnostic.ProductParameterField("name", Some("desc"), Agnostic.ValueParameter("string"))
    val outputParam = Agnostic.ProductParameterField("result", Some("result desc"), Agnostic.ValueParameter("intger"))

    val schema = Agnostic.Schema()
      .method[GetBlockByIndexRequest, BlockResponse]("testMethod", Some("testMethodDescr"))
      .method[GetBlockByHashRequest, BlockResponse]("anotherTestMethod", Some("testMethodDescr"))

    implicit lazy val encodeParam: Encoder[JsonSchema.Param] = Encoder.instance {
      case bar @ JsonSchema.RefParam(_) => bar.asJson
      case baz @ JsonSchema.ValParam(_) => baz.asJson
      case qux @ JsonSchema.ArrParam(_, _) => qux.asJson
      case foo @ JsonSchema.CoproductDefn(_) => foo.asJson
    }

    val orpc = OpenRpc.createSchema(schema).asJson.deepDropNullValues.spaces2
    
    println(orpc)

    // val orpc = OpenRpc.Schema(
    //   info = OpenRpc.Info(
    //     version = "1.0.0",
    //     title = "Test schema",
    //     description = "About to test and validate schema",
    //     termsOfService = None,
    //     contact = None,
    //     license = None
    //   ),
    //   servers = Nil,
    // )

  }
  
}