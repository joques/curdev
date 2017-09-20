package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{DraftValidation, DraftValidationJsonImplicits}

final case class CurriculumDevelopmentDraftValidationRequestMessage(messageId: String, content: DraftValidation) extends ComplexRequestMessage[DraftValidation](messageId, content)

object CurriculumDevelopmentDraftValidationRequestMessageJsonImplicits {
    implicit val draftValFormat: Format[DraftValidation] =  DraftValidationJsonImplicits.draftValFmt

    implicit val cdDraftValRequestMessageFmt = Json.format[CurriculumDevelopmentDraftValidationRequestMessage]
    implicit val cdDraftValRequestMessageeWrites = Json.writes[CurriculumDevelopmentDraftValidationRequestMessage]
    implicit val cdDraftValRequestMessageReads = Json.reads[CurriculumDevelopmentDraftValidationRequestMessage]
}
