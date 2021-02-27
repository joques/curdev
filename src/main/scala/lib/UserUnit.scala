package yester.lib

import io.leonard.TraitFormat.{traitFormat}
import play.api.libs.json.{Json, Format}

import com.couchbase.client.scala.implicits.Codec

sealed trait UserUnit
case class AcademicUnit(faculty: String, department: String) extends UserUnit
case class AdministrativeUnit(office: String, section: String) extends UserUnit

object AcademicUnit {
    implicit val acaUnitCodec: Codec[AcademicUnit] = Codec.codec[AcademicUnit]

    implicit val useracUnitFormat: Format[UserUnit] =  traitFormat[UserUnit] << Json.format[AcademicUnit] << Json.format[AdministrativeUnit]
}

object AdministrativeUnit {
    implicit val adUnitCodec: Codec[AdministrativeUnit] = Codec.codec[AdministrativeUnit]

    // implicit val useradUnitFormat: Format[UserUnit] =  traitFormat[UserUnit] << Json.format[AcademicUnit] << Json.format[AdministrativeUnit]
}
