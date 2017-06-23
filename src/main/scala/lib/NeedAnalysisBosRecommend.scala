package yester.lib

import play.api.libs.json.Json


final case class NeedAnalysisBosRecommend(devCode: String, date: String, status: Boolean, commitHash: String)

object NeedAnalysisBosRecommendJsonImplicits {
    implicit val needAnaBRFmt = Json.format[NeedAnalysisBosRecommend]
    implicit val needAnaBRWrites = Json.writes[NeedAnalysisBosRecommend]
    implicit val needAnaBRReads = Json.reads[NeedAnalysisBosRecommend]
}
