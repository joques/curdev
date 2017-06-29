package yester.lib

import play.api.libs.json.Json


final case class NeedAnalysisConclude(devCode: String, decision: String, commitHash: Option[String])

object NeedAnalysisConcludeJsonImplicits {
    implicit val needAnaConclFmt = Json.format[NeedAnalysisConclude]
    implicit val needAnaConclWrites = Json.writes[NeedAnalysisConclude]
    implicit val needAnaConclReads = Json.reads[NeedAnalysisConclude]
}
