pacakge yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{NeedAnalysisConsultation,NeedAnalysisConsultationJsonImplicits}

final case class NeedAnalysisConsultationRequestMessage(messageId: String, content: NeedAnalysisConsultation) extends ComplexRequestMessage[NeedAnalysisConsultation](messageId, content)

object NeedAnalysisConsultationRequestMessageJsonImplicits {
    implicit val needAnaConsFormat: Format[NeedAnalysisConsultation] =  NeedAnalysisConsultationJsonImplicits.needAnaConsFmt

    implicit val needAnaConsRequestMessageFmt = Json.format[NeedAnalysisConsultationRequestMessage]
    implicit val needAnaConsRequestMessageeWrites = Json.writes[NeedAnalysisConsultationRequestMessage]
    implicit val needAnaConsRequestMessageReads = Json.reads[NeedAnalysisConsultationRequestMessage]
}
