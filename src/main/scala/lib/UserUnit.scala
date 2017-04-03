package yester.lib

// import play.api.libs.json.{Format}
import julienrf.json.derived._

sealed trait UserUnit

case class AcademicUnit(faculty: Int, department: Int) extends UserUnit
case class AdministrativeUnit(office: Int, section: Int) extends UserUnit

object UserUnitJsonImplicits {
    implicit lazy val userUnitFmt: OFormat[UserUnit] = flat.oformat((__ \ "type").format[String])
}

// object AcademicUnitJsonImplicits {
//     implicit val acaUnitFmt = Json.format[AcademicUnit]
//     implicit val acaUnitWrites = Json.writes[AcademicUnit]
//     implicit val acaUnitReads = Json.reads[AcademicUnit]
// }
//
//
// object AdministrativeUnitJsonImplicits {
//     implicit val adUnitFmt = Json.format[AdministrativeUnit]
//     implicit val adUnitWrites = Json.writes[AdministrativeUnit]
//     implicit val adUnitReads = Json.reads[AdministrativeUnit]
// }
