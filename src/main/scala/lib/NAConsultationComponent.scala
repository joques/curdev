package yester.lib

import play.api.libs.json.Json

final case class NAConsultationComponent(date: String, organization: String, commitHash: Option[String])

object NAConsultationComponentJsonImplicits {
    implicit val naConsCompFmt = Json.format[NAConsultationComponent]
    implicit val naConsCompWrites = Json.writes[NAConsultationComponent]
    implicit val naConsCompReads = Json.reads[NAConsultationComponent]
}
