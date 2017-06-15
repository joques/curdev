package yester.lib

import play.api.libs.json.Json


final case class NeedAnalysisSurvey(decision: Boolean, devCode: String, commitHash: Option[String])


object NeedAnalysisSurveyJsonImplicits {
    implicit val needAnaSurvFmt = Json.format[NeedAnalysisSurvey]
    implicit val needAnaSurvWrites = Json.writes[NeedAnalysisSurvey]
    implicit val needAnaSurvReads = Json.reads[NeedAnalysisSurvey]
}
