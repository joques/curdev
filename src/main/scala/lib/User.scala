package yester.lib

import play.api.libs.json.Json

final case class User(username: String, password: String, profile: String, firstname: String, lastname: String, faculty: Int, department: Int)

object UserJsonImplicits {
    implicit val userFmt = Json.format[User]
    implicit val userWrites = Json.writes[User]
    implicit val userReads = Json.reads[User]
}
