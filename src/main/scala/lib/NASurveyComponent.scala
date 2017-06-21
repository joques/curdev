package yester.lib

import play.api.libs.json.Json

final case class NASurveyComponent(commitHash: String)

object NASurveyComponentJsonImplicits {
    implicit val naSurvCompFmt = Json.format[NASurveyComponent]
    implicit val naSurvCompWrites = Json.writes[NASurveyComponent]
    implicit val naSurvCompReads = Json.reads[NASurveyComponent]
}
