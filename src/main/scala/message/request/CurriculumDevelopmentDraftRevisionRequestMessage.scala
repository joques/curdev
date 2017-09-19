package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{DraftRevision, DraftRevisionJsonImplicits}

final case class CurriculumDevelopmentDraftRevisionRequestMessage(messageId: String, content: DraftRevision) extends ComplexRequestMessage[DraftRevision](messageId, content)

object CurriculumDevelopmentDraftRevisionRequestMessageJsonImplicits {
    implicit val draftRevFormat: Format[DraftRevision] =  DraftRevisionJsonImplicits.draftRevFmt

    implicit val cdDraftRevRequestMessageFmt = Json.format[CurriculumDevelopmentDraftRevisionRequestMessage]
    implicit val cdDraftRevRequestMessageeWrites = Json.writes[CurriculumDevelopmentDraftRevisionRequestMessage]
    implicit val cdDraftRevRequestMessageReads = Json.reads[CurriculumDevelopmentDraftRevisionRequestMessage]
}
