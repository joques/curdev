import play.api.libs.json.Json

final case class PreProgrammeComponent(devCode: String, initiator: String)

object PreProgrammeComponentJsonImplicits {
    implicit val preProgCompFmt = Json.format[PreProgrammeComponent]
    implicit val preProgCompWrites = Json.writes[PreProgrammeComponent]
    implicit val preProgCompReads = Json.reads[PreProgrammeComponent]
}
