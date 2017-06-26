package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{NeedAnalysisSenateRecommend, NeedAnalysisSenateRecommendJsonImplicits}

final case class NeedAnalysisSenateRecommendRequestMessage(messageId: String, content: NeedAnalysisSenateRecommend) extends ComplexRequestMessage[NeedAnalysisSenateRecommend](messageId, content)

object NeedAnalysisSenateRecommendRequestMessageJsonImplicits {
    implicit val needAnaSRFormat: Format[NeedAnalysisSenateRecommend] =  NeedAnalysisSenateRecommendJsonImplicits.needAnaSRFmt

    implicit val needAnaSRRequestMessageFmt = Json.format[NeedAnalysisSenateRecommendRequestMessage]
    implicit val needAnaSRRequestMessageeWrites = Json.writes[NeedAnalysisSenateRecommendRequestMessage]
    implicit val needAnaSRRequestMessageReads = Json.reads[NeedAnalysisSenateRecommendRequestMessage]
}
