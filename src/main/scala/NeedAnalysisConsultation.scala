import play.api.libs.json.Json

case class NeedAnalysisConsultation(date: String, initiator: String, devCode: String)

object NeedAnalysisConsultationJsonImplicits {
    implicit val needAnaConsFmt = Json.format[NeedAnalysisConsultation]
    implicit val needAnaConsWrites = Json.writes[NeedAnalysisConsultation]
    implicit val needAnaConsReads = Json.reads[NeedAnalysisConsultation]
}
