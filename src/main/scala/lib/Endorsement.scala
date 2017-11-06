package yester.lib

import play.api.libs.json.Json

final case class Endorsement(devCode: String, endorsementDate: String, decision: String)

object EndorsementJsonImplicits {
    implicit val endFmt = Json.format[Endorsement]
    implicit val endWrites = Json.writes[Endorsement]
    implicit val endReads = Json.reads[Endorsement]
}
