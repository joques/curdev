package yester.lib

import play.api.libs.json.Json

final case class Consultation(devCode: String, consDate: String)

object ConsultationJsonImplicits {
    implicit val consFmt = Json.format[Consultation]
    implicit val consWrites = Json.writes[Consultation]
    implicit val consReads = Json.reads[Consultation]
}
