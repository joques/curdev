package yester.message.request

import play.api.libs.json.{Reads, Json, Format}
import com.couchbase.client.scala.implicits.Codec

import yester.lib.NeedAnalysisAPCRecommend

final case class NeedAnalysisAPCRecommendRequestMessage(messageId: String, content: NeedAnalysisAPCRecommend) extends ComplexRequestMessage[NeedAnalysisAPCRecommend](messageId, content)

object NeedAnalysisAPCRecommendRequestMessage {
	implicit val codec: Codec[NeedAnalysisAPCRecommendRequestMessage] = Codec.codec[NeedAnalysisAPCRecommendRequestMessage]

	implicit val needAnaAPCRRequestMessageFmt = Json.format[NeedAnalysisAPCRecommendRequestMessage]
    implicit val needAnaAPCRRequestMessageeWrites = Json.writes[NeedAnalysisAPCRecommendRequestMessage]
    implicit val needAnaAPCRRequestMessageReads = Json.reads[NeedAnalysisAPCRecommendRequestMessage]
}
