package yester.lib

import play.api.libs.json.{Format}
// import julienrf.json.derived

sealed trait UserUnit
case class AcademicUnit(faculty: String, department: String) extends UserUnit
case class AdministrativeUnit(office: String, section: String) extends UserUnit

implicit val acaUnitFmt = Json.format[AcademicUnit]
implicit val admUnitFmt = Json.format[AdministrativeUnit]

object UserUnit {
    // implicit lazy val userUnitFmt: Format[UserUnit] = derived.oformat

    def unapply(uUnit: UserUnit): Option[(String, JsValue)] = {
        val (prod: Product, sub) = uUnit match {
            case a: AcademicUnit => (a, Json.toJson(a)(acaUnitFmt))
            case a: AdministrativeUnit => (a, Json.toJson(a)(admUnitFmt))
        }
        Some(prod.productPrefix -> sub)
    }

    def apply(`class`: String, data: JsValue): UserUnit = {
        (`class` match {
            case "AcademicUnit" => Json.fromJson[AcademicUnit](data)(acaUnitFmt)
            case "AdministrativeUnit" => Json.fromJson[AdministrativeUnit](data)(admUnitFmt)
        }).get
    }
}
