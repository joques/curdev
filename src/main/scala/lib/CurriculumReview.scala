package yester.lib

import play.api.libs.json.Json

final case class CurriculumReview(devCode: String, code: String, initiator: String)

object CurriculumReviewJsonImplicits {
    implicit val crvFmt = Json.format[CurriculumReview]
    implicit val crvWrites = Json.writes[CurriculumReview]
    implicit val crvReads = Json.reads[CurriculumReview]
}
