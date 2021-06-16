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

    val schema = Agnostic.Schema()
      .method[AuthorizationSignRequest, AuthorizationSignResponse]("authorizationSign", Some("testMethodDescr"))
      .method[GetEtcSnapshotBalanceWithProofRequest, GetEtcSnapshotBalanceWithProofResponse]("getEtcSnapshotBalanceWithProof", Some("testMethodDescr"))
      .method[MiningRequest, MiningResponse]("mine", Some("testMethodDescr"))
      .method[GetMiningStateRequest, GetMiningStateResponse]("getMiningState", Some("testMethodDescr"))
      .method[CancelMiningRequest, CancelMiningResponse]("cancelMining", Some("testMethodDescr"))


    implicit lazy val encodeParam: Encoder[JsonSchema.Param] = Encoder.instance {
      case bar @ JsonSchema.RefParam(_) => bar.asJson
      case baz @ JsonSchema.ValParam(_) => baz.asJson
      case qux @ JsonSchema.ArrParam(_, _) => qux.asJson
      case foo @ JsonSchema.CoproductDefn(_) => foo.asJson
    }

    val orpc = OpenRpc.createSchema(schema).asJson.deepDropNullValues.spaces2
    
    println(orpc)

  }
  
}