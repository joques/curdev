package yester.lib

import play.api.libs.json.Json

final case class NASenateComponent(date: String, status: String, commitHash: Option[String])

object NASenateComponentJsonImplicits {
    implicit val naSenateCompFmt = Json.format[NASenateComponent]
    implicit val naSenateCompWrites = Json.writes[NASenateComponent]
    implicit val naSenateCompReads = Json.reads[NASenateComponent]
}
