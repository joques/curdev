package yester.lib

import play.api.libs.json.{Json, Format}
import com.couchbase.client.scala.implicits.Codec

import User._

final case class UserWithPreProgramme(userDetails: User, preProgrammeCodes: Option[Seq[String]])

object UserWithPreProgramme {
    implicit val userWPPCodec: Codec[UserWithPreProgramme] = Codec.codec[UserWithPreProgramme]

    implicit val uwPPFmt = Json.format[UserWithPreProgramme]
    implicit val uwPPWrites = Json.writes[UserWithPreProgramme]
    implicit val uwPPReads = Json.reads[UserWithPreProgramme]
}
