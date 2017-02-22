import play.api.libs.json.{Reads, Json, Format}

case class UserWithPreProgrammeResponseMessage(messageId: String, operationError: Option[String], operationResult: Option[UserWithPreProgramme]) extends ResponseMessage[UserWithPreProgramme](messageId, operationError, operationResult)

object UserWithPreProgrammeResponseMessageJsonImplicits {
    implicit val uwPPFmt: Format[UserWithPreProgramme] =  UserWithPreProgrammeJsonImplicits.uwPPFmt

    implicit val uwPPResponseMessageFmt = Json.format[UserWithPreProgrammeResponseMessage]
    implicit val uwPPResponseMessageWrites = Json.writes[UserWithPreProgrammeResponseMessage]
    implicit val uwPPResponseMessageReads = Json.reads[UserWithPreProgrammeResponseMessage]
}
