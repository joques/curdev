package yester.lib

import akka.util.ByteString
import play.api.libs.json.{Json, Format, Writes}
import org.reactivecouchbase.rs.scaladsl.json.{JsonReads, JsonWrites, JsonFormat, JsonSuccess, JsonResult, JsonError}
import UserProfile._

final case class User(username: String, password: String, profile: UserProfile, firstname: String, lastname: String, usrUnit: UserUnit, emailAddress: String)

object UserJsonImplicits {
    implicit val userUnitFormat: Format[UserUnit] =  UserUnitJsonImplicits.userUnitFormat

    implicit val userFmt = Json.format[User]
    implicit val userWrites = Json.writes[User]
    implicit val userReads = Json.reads[User]
		
	def convertJsonFormat[User](modelFormat: Format[User]): JsonFormat[User] =
    JsonFormat[User](
      JsonReads[User](
        bs =>
          modelFormat
            .reads(Json.parse(bs.utf8String))
            .map(result => JsonSuccess(result))
            .getOrElse[JsonResult[User]](JsonError())
      ),
      JsonWrites[User](jsv => ByteString(Json.stringify(modelFormat.writes(jsv))))
    )
	
	implicit val userJsonFormat: JsonFormat[User] = convertJsonFormat(userFmt)
}
