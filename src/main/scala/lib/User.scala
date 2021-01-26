package yester.lib

import play.api.libs.json.{Json, Format}
import org.reactivecouchbase.rs.scaladsl.json.{JsonReads, JsonWrites, JsonFormat, JsonSuccess}
import UserProfile._

final case class User(username: String, password: String, profile: UserProfile, firstname: String, lastname: String, usrUnit: UserUnit, emailAddress: String)

object UserJsonImplicits {
    implicit val userUnitFormat: Format[UserUnit] =  UserUnitJsonImplicits.userUnitFormat

    implicit val userFmt = Json.format[User]
    implicit val userWrites = Json.writes[User]
    implicit val userReads = Json.reads[User]
	
	implicit val userJsonReads: JsonReads[User] = JsonReads(bs => JsonSuccess(Json.parse(bs.utf8String)))
	implicit val userJsonWrites: JsonWrites[User] = JsonWrites(jsv => ByteString(Json.stringify(jsv)))
	implicit val defaultUserFormat: JsonFormat[User] = JsonFormat(read, write)
}
