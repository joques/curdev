package yester.message.request

// import play.api.libs.json.{Reads, Json, Format}
import com.couchbase.client.scala.implicits.Codec

import yester.lib.NeedAnalysisSenateStart

final case class NeedAnalysisSenateStartRequestMessage(messageId: String, content: NeedAnalysisSenateStart) extends ComplexRequestMessage[NeedAnalysisSenateStart](messageId, content)

object NeedAnalysisSenateStartRequestMessage {
	implicit val codec: Codec[NeedAnalysisSenateStartRequestMessage] = Codec.codec[NeedAnalysisSenateStartRequestMessage]
}

// object NeedAnalysisSenateStartRequestMessageJsonImplicits {
//     implicit val needAnaSSFormat: Format[NeedAnalysisSenateStart] =  NeedAnalysisSenateStartJsonImplicits.needAnaSSFmt

//     implicit val needAnaSSRequestMessageFmt = Json.format[NeedAnalysisSenateStartRequestMessage]
//     implicit val needAnaSSRequestMessageeWrites = Json.writes[NeedAnalysisSenateStartRequestMessage]
//     implicit val needAnaSSRequestMessageReads = Json.reads[NeedAnalysisSenateStartRequestMessage]
// }
