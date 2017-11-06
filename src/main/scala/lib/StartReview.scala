package yester.lib

import play.api.libs.json.Json

final case class StartReview(devCode: String, reviewDate: String)

object StartReviewJsonImplicits {
    implicit val stReviewFmt = Json.format[StartReview]
    implicit val stReviewWrites = Json.writes[StartReview]
    implicit val stReviewReads = Json.reads[StartReview]
}
