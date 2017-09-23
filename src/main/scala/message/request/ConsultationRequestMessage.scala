package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{Consultation, ConsultationJsonImplicits}

final case class ConsultationRequestMessage(messageId: String, content: Consultation) extends ComplexRequestMessage[Consultation](messageId, content)

object ConsultationRequestMessageRequestMessageJsonImplicits {
    implicit val consFormat: Format[Consultation] =  ConsultationJsonImplicits.consFmt

    implicit val consRequestMessageFmt = Json.format[ConsultationRequestMessage]
    implicit val consRequestMessageeWrites = Json.writes[ConsultationRequestMessage]
    implicit val consRequestMessageReads = Json.reads[ConsultationRequestMessage]
}
