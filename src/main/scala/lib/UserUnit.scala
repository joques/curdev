package yester.lib

import play.api.libs.json.{Format}
import julienrf.json.derived

sealed trait UserUnit

case class AcademicUnit(faculty: String, department: String) extends UserUnit
case class AdministrativeUnit(office: String, section: String) extends UserUnit

object UserUnit {
    implicit lazy val userUnitFmt: Format[UserUnit] = derived.oformat
}
