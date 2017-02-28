import play.api.libs.json.Json

final case class SimpleRequestMessage (messageId: String, content: String)

object SimpleRequestMessageJsonImplicits {
    implicit val simpleRequestMessageFmt = Json.format[SimpleRequestMessage]
    implicit val simpleRequestMessageWrites = Json.writes[SimpleRequestMessage]
    implicit val simpleRequestMessageReads = Json.reads[SimpleRequestMessage]
}
