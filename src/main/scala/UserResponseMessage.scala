import play.api.libs.json.{Reads, Json, Format}

final case class UserResponseMessage(messageId: String, operationError: Option[String], operationResult: Option[User]) extends ResponseMessage[User](messageId, operationError, operationResult)

object UserResponseMessageJsonImplicits {
    implicit val userFormat: Format[User] =  UserJsonImplicits.userFmt

    implicit val userResponseMessageFmt = Json.format[UserResponseMessage]
    implicit val userResponseMessageWrites = Json.writes[UserResponseMessage]
    implicit val userResponseMessageReads = Json.reads[UserResponseMessage]
}
