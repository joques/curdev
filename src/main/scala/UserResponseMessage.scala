import play.api.libs.json.Json

case class UserResponseMessage[User](messageId: String, operationError: String, operationResult: User) extends ResponseMessage[User](messageId, operationError, operationResult)

object UserResponseMessageJsonImplicits {
    implicit val userResponseMessageFmt = Json.format[UserResponseMessage]
    implicit val userResponseMessageWrites = Json.writes[UserResponseMessage]
    implicit val userResponseMessageReads = Json.reads[UserResponseMessage]
}
