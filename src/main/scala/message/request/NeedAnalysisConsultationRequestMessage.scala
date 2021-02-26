package yester.message.request

// import play.api.libs.json.{Reads, Json, Format}

import com.couchbase.client.scala.implicits.Codec

import yester.lib.NeedAnalysisConsultation

final case class NeedAnalysisConsultationRequestMessage(messageId: String, content: NeedAnalysisConsultation) extends ComplexRequestMessage[NeedAnalysisConsultation](messageId, content)

object NeedAnalysisConsultationRequestMessage {
	implicit val codec: Codec[NeedAnalysisConsultationRequestMessage] = Codec.codec[NeedAnalysisConsultationRequestMessage]
}

// object NeedAnalysisConsultationRequestMessageJsonImplicits {
//     implicit val needAnaConsFormat: Format[NeedAnalysisConsultation] =  NeedAnalysisConsultationJsonImplicits.needAnaConsFmt

//     implicit val needAnaConsRequestMessageFmt = Json.format[NeedAnalysisConsultationRequestMessage]
//     implicit val needAnaConsRequestMessageeWrites = Json.writes[NeedAnalysisConsultationRequestMessage]
//     implicit val needAnaConsRequestMessageReads = Json.reads[NeedAnalysisConsultationRequestMessage]
// }
