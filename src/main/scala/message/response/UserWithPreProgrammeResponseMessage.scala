package yester.message.response

import play.api.libs.json.{Reads, Json, Format}
import yester.lib.{UserWithPreProgramme, UserWithPreProgrammeJsonImplicits}

final case class UserWithPreProgrammeResponseMessage(messageId: String, operationError: Option[String], operationResult: Option[UserWithPreProgramme]) extends ResponseMessage[UserWithPreProgramme](messageId, operationError, operationResult)

object UserWithPreProgrammeResponseMessageJsonImplicits {
    implicit val uwPPFmt: Format[UserWithPreProgramme] =  UserWithPreProgrammeJsonImplicits.uwPPFmt

    implicit val uwPPResponseMessageFmt = Json.format[UserWithPreProgrammeResponseMessage]
    implicit val uwPPResponseMessageWrites = Json.writes[UserWithPreProgrammeResponseMessage]
    implicit val uwPPResponseMessageReads = Json.reads[UserWithPreProgrammeResponseMessage]
}
