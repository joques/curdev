package yester.lib

import play.api.libs.json.Json


final case class NeedAnalysisBosStart(devCode: String, date: String)

object NeedAnalysisBosStartJsonImplicits {
    implicit val needAnaBSFmt = Json.format[NeedAnalysisBosStart]
    implicit val needAnaBSWrites = Json.writes[NeedAnalysisBosStart]
    implicit val needAnaBSReads = Json.reads[NeedAnalysisBosStart]
}
