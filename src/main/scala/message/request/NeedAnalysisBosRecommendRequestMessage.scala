package yester.message.request

// import play.api.libs.json.{Reads, Json, Format}
import com.couchbase.client.scala.implicits.Codec

import yester.lib.NeedAnalysisBosRecommend

final case class NeedAnalysisBosRecommendRequestMessage(messageId: String, content: NeedAnalysisBosRecommend) extends ComplexRequestMessage[NeedAnalysisBosRecommend](messageId, content)

object NeedAnalysisBosRecommendRequestMessage {
	implicit val codec: Codec[NeedAnalysisBosRecommendRequestMessage] = Codec.codec[NeedAnalysisBosRecommendRequestMessage]
}

// object NeedAnalysisBosRecommendRequestMessageJsonImplicits {
//     implicit val needAnaBRFormat: Format[NeedAnalysisBosRecommend] =  NeedAnalysisBosRecommendJsonImplicits.needAnaBRFmt

//     implicit val needAnaBRRequestMessageFmt = Json.format[NeedAnalysisBosRecommendRequestMessage]
//     implicit val needAnaBRRequestMessageeWrites = Json.writes[NeedAnalysisBosRecommendRequestMessage]
//     implicit val needAnaBRRequestMessageReads = Json.reads[NeedAnalysisBosRecommendRequestMessage]
// }
