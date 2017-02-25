import play.api.libs.json.Json

case class FindUserRequestMessage (messageId: String, content: String) extends SimpleRequestMessage(messageId, content)

object FindUserRequestMessageJsonImplicits {
    implicit val fuRequestMessageFmt = Json.format[FindUserRequestMessage]
    implicit val fuRequestMessageWrites = Json.writes[FindUserRequestMessage]
    implicit val fuRequestMessageReads = Json.reads[FindUserRequestMessage]
}
