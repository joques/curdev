package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{RecommendReview, RecommendReviewJsonImplicits}

final case class RecommendReviewRequestMessage(messageId: String, content: RecommendReview) extends ComplexRequestMessage[RecommendReview](messageId, content)

object RecommendReviewRequestMessageJsonImplicits {
    implicit val rcReviewFormat: Format[RecommendReview] =  RecommendReviewJsonImplicits.rcReviewFmt

    implicit val rRevRequestMessageFmt = Json.format[RecommendReviewRequestMessage]
    implicit val rRevRequestMessageeWrites = Json.writes[RecommendReviewRequestMessage]
    implicit val rRevRequestMessageReads = Json.reads[RecommendReviewRequestMessage]
}
