import play.api.libs.json.json

case class SimpleRequestMessage (messageId: String, requestContent: String)

object SimpleRequestMessageJsonImplicits {
    implicit val simpleRequestMessageFmt = Json.format[SimpleRequestMessage]
    implicit val simpleRequestMessageWrites = Json.writes[SimpleRequestMessage]
    implicit val simpleRequestMessageReads = Json.reads[SimpleRequestMessage]
}
