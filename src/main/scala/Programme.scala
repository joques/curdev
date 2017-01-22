import play.api.libs.json.Json

case class Programme(faculty: String, department: String, programme: String)

object ProgrammeJsonImplicits {
    implicit val prgFmt = Json.format[Programme]
    implicit val prgWrites = Json.writes[Programme]
    implicit val prgReads = Json.reads[Programme]
}
