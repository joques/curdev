import play.api.libs.json.Json

case class User(username: String, password: String, profile: String, faculty: String, department: String)

object UserJsonImplicits {
    implicit val userFmt = Json.format[User]
    implicit val userWrites = Json.writes[User]
    implicit val userReads = Json.reads[User]
}
