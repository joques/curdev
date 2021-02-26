package yester.message.request

import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec

final case class SimpleRequestMessage (messageId: String, content: String)

object SimpleRequestMessage {
	implicit val codec: Codec[SimpleRequestMessage] = Codec.codec[SimpleRequestMessage]

	implicit val simpleRequestMessageFmt = Json.format[SimpleRequestMessage]
    implicit val simpleRequestMessageWrites = Json.writes[SimpleRequestMessage]
    implicit val simpleRequestMessageReads = Json.reads[SimpleRequestMessage]
}