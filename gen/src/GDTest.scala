package gen.gd

case class AuthorizationSignRequest(etcPrivateKey: String, midnightAddress: String)
case class AuthorizationSignResponse(signature: String)

case class GetEtcSnapshotBalanceWithProofRequest(address: String)
case class GetEtcSnapshotBalanceWithProofResponse(resp: EtcSnapshotResponse)

case class MiningRequest(balance: String, etcAddress: String, unlockPhase: Option[String] = None)
case class MiningResponse(state: MiningStateResponse)

case class GetMiningStateRequest()
case class GetMiningStateResponse(state: MiningStateResponse)

sealed trait MiningStateResponse
object MiningStateResponse {
  case class MiningSuccessful(mixHash: String, nonce: String) extends MiningStateResponse
  case class MiningUnsuccessful(msg: String) extends MiningStateResponse
  case object MiningNotStarted extends MiningStateResponse
  case class MiningInProgress(currentNonce: String) extends MiningStateResponse
  // case class NewMineStarted(estimatedTime: Duration, estimatedBlockOfTxInclusion: BigInt) extends MiningStateResponse
  case class NewMineStarted(estimatedTime: String, estimatedBlockOfTxInclusion: Int) extends MiningStateResponse
}

case class CancelMiningRequest()
case class CancelMiningResponse(cancelResponse: CancelResponse)

sealed trait EtcSnapshotResponse
object EtcSnapshotResponse {
  case class AccountBalanceWithProof(balance: String, proof: String) extends EtcSnapshotResponse
  case object AccountDoesNotExist extends EtcSnapshotResponse
  case object SnapshotDisabled extends EtcSnapshotResponse
}

case class UnlockPhase(startBlockNumber: BigInt, stopBlockNumber: BigInt)

sealed trait CancelResponse
case object MiningCanceled extends CancelResponse
case object NoMiningToCancel extends CancelResponse