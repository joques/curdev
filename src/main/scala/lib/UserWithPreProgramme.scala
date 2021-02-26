package yester.lib

// import play.api.libs.json.{Json, Format}
import com.couchbase.client.scala.implicits.Codec

import User

final case class UserWithPreProgramme(userDetails: User, preProgrammeCodes: Option[Seq[String]])

// object UserWithPreProgrammeJsonImplicits {
//     implicit val userFormat: Format[User] =  UserJsonImplicits.userFmt

//     implicit val uwPPFmt = Json.format[UserWithPreProgramme]
//     implicit val uwPPWrites = Json.writes[UserWithPreProgramme]
//     implicit val uwPPReads = Json.reads[UserWithPreProgramme]
// }
//
object UserWithPreProgramme {
    implicit val userWPPCodec: Codec[UserWithPreProgramme] = Codec.codec[UserWithPreProgramme]
}
