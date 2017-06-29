package yester.lib

import play.api.libs.json.Json

final case class NeedAnalysisSenateStart(devCode: String, date: String)

object NeedAnalysisSenateStartJsonImplicits {
    implicit val needAnaSSFmt = Json.format[NeedAnalysisSenateStart]
    implicit val needAnaSSWrites = Json.writes[NeedAnalysisSenateStart]
    implicit val needAnaSSReads = Json.reads[NeedAnalysisSenateStart]
}
