package yester.message.request

import play.api.libs.json.{Json, Format}
import com.couchbase.client.scala.implicits.Codec

final case class FindUserRequestMessage (simpleMsg: SimpleRequestMessage){}

object FindUserRequestMessage {
	implicit val codec: Codec[FindUserRequestMessage] = Codec.codec[FindUserRequestMessage]

	implicit val fuRequestMessageFmt = Json.format[FindUserRequestMessage]
    implicit val fuRequestMessageWrites = Json.writes[FindUserRequestMessage]
    implicit val fuRequestMessageReads = Json.reads[FindUserRequestMessage]
}