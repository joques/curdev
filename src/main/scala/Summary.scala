import play.api.libs.json.{Json, Format}

final case class Summary(inProgress: Option[List[Programme]], dueForReview: Option[List[Programme]], recentlyApproved: Option[List[Programme]])

object SummaryJsonImplicits {
    implicit val prgFormat: Format[Programme] =  ProgrammeJsonImplicits.prgFmt

    implicit val summaryFmt = Json.format[Summary]
    implicit val summaryWrites = Json.writes[Summary]
    implicit val summaryReads = Json.reads[Summary]
}
