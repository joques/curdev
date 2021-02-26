package yester.message.request

import com.couchbase.client.scala.implicits.Codec
import play.api.libs.json.{Reads, Json, Format}

import yester.lib.DraftSubmission

final case class CurriculumDevelopmentDraftSubmissionRequestMessage(messageId: String, content: DraftSubmission) extends ComplexRequestMessage[DraftSubmission](messageId, content)

object CurriculumDevelopmentDraftSubmissionRequestMessage {
	implicit val codec: Codec[CurriculumDevelopmentDraftSubmissionRequestMessage] = Codec.codec[CurriculumDevelopmentDraftSubmissionRequestMessage]

	implicit val cdDraftSubRequestMessageFmt = Json.format[CurriculumDevelopmentDraftSubmissionRequestMessage]
    implicit val cdDraftSubRequestMessageeWrites = Json.writes[CurriculumDevelopmentDraftSubmissionRequestMessage]
    implicit val cdDraftSubRequestMessageReads = Json.reads[CurriculumDevelopmentDraftSubmissionRequestMessage]
}