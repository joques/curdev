package yester.lib

// import play.api.libs.json.{Format}
import julienrf.json.derived.flat

sealed trait UserUnit

case class AcademicUnit(faculty: Int, department: Int) extends UserUnit
case class AdministrativeUnit(office: Int, section: Int) extends UserUnit

object UserUnitJsonImplicits {
    implicit lazy val userUnitFmt: OFormat[UserUnit] = flat.oformat((__ \ "type").format[String])
}
