package yester.lib

import io.leonard.TraitFormat.{traitFormat}
import play.api.libs.json.{Json, Format}
// import julienrf.json.derived

sealed trait UserUnit
case class AcademicUnit(faculty: String, department: String) extends UserUnit
case class AdministrativeUnit(office: String, section: String) extends UserUnit

object UserUnitJsonImplicits {
    implicit val userUnitFormat: Format[UserUnit] =  traitFormat[UserUnit] << Json.format[AcademicUnit] << Json.format[AdministrativeUnit]
}
