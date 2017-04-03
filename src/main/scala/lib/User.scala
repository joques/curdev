package yester.lib

import play.api.libs.json.Json
import UserProfile._

final case class User(username: String, password: String, profile: UserProfile, firstname: String, lastname: String, unit: UserUnit)

object UserJsonImplicits {
    implicit val academicUnitFormat: Format[AcademicUnit] =  AcademicUnitJsonImplicits.acaUnitFmt
    implicit val administrativeUnitFormat: Format[AdministrativeUnit] =  AdministrativeUnitJsonImplicits.adUnitFmt

    implicit val userFmt = Json.format[User]
    implicit val userWrites = Json.writes[User]
    implicit val userReads = Json.reads[User]
}
