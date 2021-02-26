package yester.message.request

// import play.api.libs.json.{Reads, Json, Format}

import com.couchbase.client.scala.implicits.Codec
import yester.lib.NeedAnalysisBosStart

final case class NeedAnalysisBosStartRequestMessage(messageId: String, content: NeedAnalysisBosStart) extends ComplexRequestMessage[NeedAnalysisBosStart](messageId, content)

object NeedAnalysisBosStartRequestMessage {
	implicit val codec: Codec[NeedAnalysisBosStartRequestMessage] = Codec.codec[NeedAnalysisBosStartRequestMessage]
}


// object NeedAnalysisBosStartRequestMessageJsonImplicits {
//     implicit val needAnaBSFormat: Format[NeedAnalysisBosStart] =  NeedAnalysisBosStartJsonImplicits.needAnaBSFmt

//     implicit val needAnaBSRequestMessageFmt = Json.format[NeedAnalysisBosStartRequestMessage]
//     implicit val needAnaBSRequestMessageeWrites = Json.writes[NeedAnalysisBosStartRequestMessage]
//     implicit val needAnaBSRequestMessageReads = Json.reads[NeedAnalysisBosStartRequestMessage]
// }
