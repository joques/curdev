package yester.lib

import play.api.libs.json.Json

final case class NAConclusionComponent(decision: String, commitHash: Option[String])

object NAConclusionComponentJsonImplicits {
    implicit val naConclCompFmt = Json.format[NAConclusionComponent]
    implicit val naConclCompWrites = Json.writes[NAConclusionComponent]
    implicit val naConclCompReads = Json.reads[NAConclusionComponent]
}
