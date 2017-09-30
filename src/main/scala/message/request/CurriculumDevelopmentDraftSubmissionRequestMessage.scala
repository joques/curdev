package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{DraftSubmission, DraftSubmissionJsonImplicits}

final case class CurriculumDevelopmentDraftSubmissionRequestMessage(messageId: String, content: DraftSubmission) extends ComplexRequestMessage[DraftSubmission](messageId, content)

object CurriculumDevelopmentDraftSubmissionRequestMessageJsonImplicits {
    implicit val draftSubFormat: Format[DraftSubmission] =  DraftSubmissionJsonImplicits.draftSubFmt

    implicit val cdDraftSubRequestMessageFmt = Json.format[CurriculumDevelopmentDraftSubmissionRequestMessage]
    implicit val cdDraftSubRequestMessageeWrites = Json.writes[CurriculumDevelopmentDraftSubmissionRequestMessage]
    implicit val cdDraftSubRequestMessageReads = Json.reads[CurriculumDevelopmentDraftSubmissionRequestMessage]
}
