package yester.lib

import play.api.libs.json.{Format}
import julienrf.json.derived

sealed trait UserUnit

case class AcademicUnit(faculty: Int, department: Int) extends UserUnit
case class AdministrativeUnit(office: Int, section: Int) extends UserUnit

object UserUnit {
    implicit lazy val userUnitFmt: Format[UserUnit] = derived.oformat
}
