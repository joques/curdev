package yester.message.request

import com.couchbase.client.scala.implicits.Codec
import play.api.libs.json.{Reads, Json, Format}
import yester.lib.CurriculumReview

final case class CurriculumReviewRequestMessage(messageId: String, content: CurriculumReview) extends ComplexRequestMessage[CurriculumReview](messageId, content)

object CurriculumReviewRequestMessage {
	implicit val codec: Codec[CurriculumReviewRequestMessage] = Codec.codec[CurriculumReviewRequestMessage]

	implicit val crvRequestMessageFmt = Json.format[CurriculumReviewRequestMessage]
    implicit val crvRequestMessageeWrites = Json.writes[CurriculumReviewRequestMessage]
    implicit val crvRequestMessageReads = Json.reads[CurriculumReviewRequestMessage]
}