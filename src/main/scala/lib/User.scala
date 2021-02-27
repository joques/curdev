package yester.lib

import play.api.libs.json.{Json, Format, Writes}
import com.couchbase.client.scala.implicits.Codec

import UserProfile._
import AcademicUnit._
import AdministrativeUnit._

final case class User(username: String, password: String, profile: UserProfile, firstname: String, lastname: String, usrUnit: UserUnit, emailAddress: String)

object User {
	implicit val codec: Codec[User] = Codec.codec[User]

	// implicit val uunitFmt: Format[UserUnit] = useracUnitFormat

	implicit val userFmt = Json.format[User]
    implicit val userWrites = Json.writes[User]
    implicit val userReads = Json.reads[User]
}
