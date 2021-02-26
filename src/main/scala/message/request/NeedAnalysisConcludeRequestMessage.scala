package yester.message.request

import play.api.libs.json.{Reads, Json, Format}
import com.couchbase.client.scala.implicits.Codec

import yester.lib.NeedAnalysisConclude

final case class NeedAnalysisConcludeRequestMessage(messageId: String, content: NeedAnalysisConclude) extends ComplexRequestMessage[NeedAnalysisConclude](messageId, content)

object NeedAnalysisConcludeRequestMessage {
	implicit val codec: Codec[NeedAnalysisConcludeRequestMessage] = Codec.codec[NeedAnalysisConcludeRequestMessage]

	implicit val needAnaConclRequestMessageFmt = Json.format[NeedAnalysisConcludeRequestMessage]
    implicit val needAnaConclRequestMessageeWrites = Json.writes[NeedAnalysisConcludeRequestMessage]
    implicit val needAnaConclRequestMessageReads = Json.reads[NeedAnalysisConcludeRequestMessage]
}