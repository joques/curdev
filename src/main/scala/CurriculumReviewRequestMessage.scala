import play.api.libs.json.{Reads, Json, Format}

case class CurriculumReviewRequestMessage(messageId: String, content: Programme) extends ComplexRequestMessage[Programme](messageId, content)

object CurriculumReviewRequestMessageJsonImplicits {
    implicit val crvFormat: Format[CurriculumReview] =  CurriculumReviewJsonImplicits.crvFmt

    implicit val crvRequestMessageFmt = Json.format[CurriculumReviewRequestMessage]
    implicit val crvRequestMessageeWrites = Json.writes[CurriculumReviewRequestMessage]
    implicit val crvRequestMessageReads = Json.reads[CurriculumReviewRequestMessage]
}
