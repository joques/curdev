package yester.message.request

import com.couchbase.client.scala.implicits.Codec
import play.api.libs.json.{Reads, Json, Format}
import yester.lib.Programme

final case class ProgrammeRequestMessage(messageId: String, content: Programme) extends ComplexRequestMessage[Programme](messageId, content)

object ProgrammeRequestMessage {
	implicit val codec: Codec[ProgrammeRequestMessage] = Codec.codec[ProgrammeRequestMessage]

	implicit val programmeRequestMessageFmt = Json.format[ProgrammeRequestMessage]
    implicit val programmeRequestMessageeWrites = Json.writes[ProgrammeRequestMessage]
    implicit val programmeRequestMessageReads = Json.reads[ProgrammeRequestMessage]
}