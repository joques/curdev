package yester.lib

import play.api.libs.json.Json

final case class NeedAnalysisConsultation(date: String, initiator: String, devCode: String, commitHash: Option[String])

object NeedAnalysisConsultationJsonImplicits {
    implicit val needAnaConsFmt = Json.format[NeedAnalysisConsultation]
    implicit val needAnaConsWrites = Json.writes[NeedAnalysisConsultation]
    implicit val needAnaConsReads = Json.reads[NeedAnalysisConsultation]
}
