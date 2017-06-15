package yester.lib

import play.api.libs.json.{Json, Format}

final case class Summary(inProgress: Option[Seq[Programme]], dueForReview: Option[Seq[Programme]], recentlyApproved: Option[Seq[Programme]])

object SummaryJsonImplicits {
    implicit val prgFormat: Format[Programme] =  ProgrammeJsonImplicits.prgFmt

    implicit val summaryFmt = Json.format[Summary]
    implicit val summaryWrites = Json.writes[Summary]
    implicit val summaryReads = Json.reads[Summary]
}
