package yester.message.request

import play.api.libs.json.{Reads, Json, Format}
import com.couchbase.client.scala.implicits.Codec
import yester.lib.Endorsement

final case class EndorsementRequestMessage(messageId: String, content: Endorsement) extends ComplexRequestMessage[Endorsement](messageId, content)

object EndorsementRequestMessage {
	implicit val codec: Codec[EndorsementRequestMessage] = Codec.codec[EndorsementRequestMessage]

	implicit val endRequestMessageFmt = Json.format[EndorsementRequestMessage]
    implicit val endRequestMessageeWrites = Json.writes[EndorsementRequestMessage]
    implicit val endRequestMessageReads = Json.reads[EndorsementRequestMessage]
}