package yester.lib

import play.api.libs.json.Json


final case class NeedAnalysisSenateRecommend(devCode: String, date: String, status: Boolean, commitHash: Option[String])

object NeedAnalysisSenateRecommendJsonImplicits {
    implicit val needAnaSRFmt = Json.format[NeedAnalysisSenateRecommend]
    implicit val needAnaSRWrites = Json.writes[NeedAnalysisSenateRecommend]
    implicit val needAnaSRReads = Json.reads[NeedAnalysisSenateRecommend]
}
