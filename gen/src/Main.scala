package gen

import SchemaMacros._

case class GetBlockByIndexRequest(i: Int)

case class GetBlockByHashRequest(hash: Int)

case class Block( 
  hash: String,
  index: Int,
  someShit: Option[String]
)

case class BlockResponse(block: Block)

// Just invoke `mill gen.run`
// Then check the result on the https://playground.open-rpc.org/
object Main {

  def main(args:Array[String]):Unit = {

    implicit val reqGen:Agnostic.Repr[GetBlockByIndexRequest] = gen[GetBlockByIndexRequest]
    implicit val respGen = gen[BlockResponse]

    // this is the ONLY thing required to define a schema
    // TODO: provide examples for each method
    val agnostic = Agnostic.Schema()
      .method[AuthorizationSignRequest, AuthorizationSignResponse]("authorizationSign", Some("testMethodDescr"))
      .method[GetEtcSnapshotBalanceWithProofRequest, GetEtcSnapshotBalanceWithProofResponse]("getEtcSnapshotBalanceWithProof", Some("testMethodDescr"))
      .method[MiningRequest, MiningResponse]("mine", Some("testMethodDescr"))
      .method[GetMiningStateRequest, GetMiningStateResponse]("getMiningState", Some("testMethodDescr"))
      .method[CancelMiningRequest, CancelMiningResponse]("cancelMining", Some("testMethodDescr"))

    val orpc = OpenRpc.createSchema(agnostic)
    
    println(orpc.jsonValue.spaces2)

  }
  
}