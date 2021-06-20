package gen

import gen.gd._
import SchemaMacros._

// Just invoke `mill gen.run`
// Then check the result on the https://playground.open-rpc.org/
object Main {

  def main(args:Array[String]):Unit = {

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