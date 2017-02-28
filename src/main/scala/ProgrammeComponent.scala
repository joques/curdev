import play.api.libs.json.Json

final case class ProgrammeComponent(approvedOn: String, nextReview: String, history: List[String], code: Option[String])

object ProgrammeComponentJsonImplicits {
    implicit val progCompFmt = Json.format[ProgrammeComponent]
    implicit val progCompWrites = Json.writes[ProgrammeComponent]
    implicit val progCompReads = Json.reads[ProgrammeComponent]
}
