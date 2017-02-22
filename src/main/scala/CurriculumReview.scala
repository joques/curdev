import play.api.libs.json.Json

case class CurriculumReview(devCode: String, code: String, initiator: String)

object CurriculumReviewJsonImplicits {
    implicit val crvFmt = Json.format[CurriculumReview]
    implicit val crvWrites = Json.writes[CurriculumReview]
    implicit val crvReads = Json.reads[CurriculumReview]
}
