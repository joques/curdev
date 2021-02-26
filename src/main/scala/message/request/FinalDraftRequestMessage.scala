package yester.message.request

import play.api.libs.json.{Reads, Json, Format}
import com.couchbase.client.scala.implicits.Codec
import yester.lib.FinalDraft

final case class FinalDraftRequestMessage(messageId: String, content: FinalDraft) extends ComplexRequestMessage[FinalDraft](messageId, content)

object FinalDraftRequestMessage {
	implicit val codec: Codec[FinalDraftRequestMessage] = Codec.codec[FinalDraftRequestMessage]

	implicit val fdRequestMessageFmt = Json.format[FinalDraftRequestMessage]
    implicit val fdRequestMessageeWrites = Json.writes[FinalDraftRequestMessage]
    implicit val fdRequestMessageReads = Json.reads[FinalDraftRequestMessage]
}