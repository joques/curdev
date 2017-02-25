import play.api.libs.json.Json

case class CreateUserRequestMessage (messageId: String, content: String) extends SimpleRequestMessage(messageId, content)

object CreateUserRequestMessageJsonImplicits {
    implicit val cuRequestMessageFmt = Json.format[FindUserRequestMessage]
    implicit val cuRequestMessageWrites = Json.writes[FindUserRequestMessage]
    implicit val cuRequestMessageReads = Json.reads[FindUserRequestMessage]
}
