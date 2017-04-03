package yester.lib

import play.api.libs.json.{Json, Format}
import UserProfile._

final case class User(username: String, password: String, profile: UserProfile, firstname: String, lastname: String, unit: UserUnit)

object UserJsonImplicits {
    implicit val userUnitFormat: Format[UserUnit] =  UserUnitJsonImplicits.userUnitFmt

    implicit val userFmt = Json.format[User]
    implicit val userWrites = Json.writes[User]
    implicit val userReads = Json.reads[User]
}
