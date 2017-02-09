import play.api.libs.json.Json

case class PreProgramme(faculty: Int, department: Int, name: String, initiator: String, level: Int)

object PreProgrammeJsonImplicits {
    implicit val preProgFmt = Json.format[PreProgramme]
    implicit val preProgWrites = Json.writes[PreProgramme]
    implicit val preProgReads = Json.reads[PreProgramme]
}
