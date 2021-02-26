package yester.message.request

import play.api.libs.json.{Reads, Json, Format}
import com.couchbase.client.scala.implicits.Codec

import yester.lib.NeedAnalysisSenateRecommend

final case class NeedAnalysisSenateRecommendRequestMessage(messageId: String, content: NeedAnalysisSenateRecommend) extends ComplexRequestMessage[NeedAnalysisSenateRecommend](messageId, content)

object NeedAnalysisSenateRecommendRequestMessage {
	implicit val codec: Codec[NeedAnalysisSenateRecommendRequestMessage] = Codec.codec[NeedAnalysisSenateRecommendRequestMessage]

	implicit val needAnaSRRequestMessageFmt = Json.format[NeedAnalysisSenateRecommendRequestMessage]
    implicit val needAnaSRRequestMessageeWrites = Json.writes[NeedAnalysisSenateRecommendRequestMessage]
    implicit val needAnaSRRequestMessageReads = Json.reads[NeedAnalysisSenateRecommendRequestMessage]
}