package yester.message.request

import play.api.libs.json.{Reads, Json, Format}
import com.couchbase.client.scala.implicits.Codec

import yester.lib.RecommendReview

final case class RecommendReviewRequestMessage(messageId: String, content: RecommendReview) extends ComplexRequestMessage[RecommendReview](messageId, content)

object RecommendReviewRequestMessage {
	implicit val codec: Codec[RecommendReviewRequestMessage] = Codec.codec[RecommendReviewRequestMessage]

	implicit val rRevRequestMessageFmt = Json.format[RecommendReviewRequestMessage]
    implicit val rRevRequestMessageeWrites = Json.writes[RecommendReviewRequestMessage]
    implicit val rRevRequestMessageReads = Json.reads[RecommendReviewRequestMessage]
}