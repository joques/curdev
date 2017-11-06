package yester.lib

import play.api.libs.json.Json

final case class RecommendReview(devCode: String, reviewUnit: String, decision: String)

object RecommendReviewJsonImplicits {
    implicit val rcReviewFmt = Json.format[RecommendReview]
    implicit val rcReviewWrites = Json.writes[RecommendReview]
    implicit val rcReviewReads = Json.reads[RecommendReview]
}
