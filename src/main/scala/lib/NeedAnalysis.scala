package yester.lib

import play.api.libs.json.{Json, Format}

final case class NeedAnalysis(consultations: Option[List[NAConsultationComponent]], survey: Option[NASurveyComponent], conclusion: Option[NAConclusionComponent])

object NeedAnalysisJsonImplicits {
    implicit val naConclCompFmt = Format[NAConclusionComponent]
    implicit val naConsCompFmt = Format[NAConsultationComponent]
    implicit val naSurvCompFmt = Format[NASurveyComponent]

    implicit val naFmt = Json.format[NeedAnalysis]
    implicit val naWrites = Json.writes[NeedAnalysis]
    implicit val naReads = Json.reads[NeedAnalysis]
}
