package yester.message.request


import com.couchbase.client.scala.implicits.Codec
import play.api.libs.json.{Reads, Json, Format}

import yester.lib.DraftValidation

final case class CurriculumDevelopmentDraftValidationRequestMessage(messageId: String, content: DraftValidation) extends ComplexRequestMessage[DraftValidation](messageId, content)

object CurriculumDevelopmentDraftValidationRequestMessage {
	implicit val codec: Codec[CurriculumDevelopmentDraftValidationRequestMessage] = Codec.codec[CurriculumDevelopmentDraftValidationRequestMessage]

	implicit val cdDraftValRequestMessageFmt = Json.format[CurriculumDevelopmentDraftValidationRequestMessage]
    implicit val cdDraftValRequestMessageeWrites = Json.writes[CurriculumDevelopmentDraftValidationRequestMessage]
    implicit val cdDraftValRequestMessageReads = Json.reads[CurriculumDevelopmentDraftValidationRequestMessage]
}