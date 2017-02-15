import play.api.libs.json.Json

case class PreProgrammeComponent(approvedOn: String, nextReview: String, history: List[String], code: Option[String])

object PreProgrammeComponentJsonImplicits {
    implicit val preProgCompFmt = Json.format[PreProgrammeComponent]
    implicit val preProgCompWrites = Json.writes[PreProgrammeComponent]
    implicit val preProgCompReads = Json.reads[PreProgrammeComponent]
}
