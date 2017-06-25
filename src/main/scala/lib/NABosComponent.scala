package yester.lib

import play.api.libs.json.Json

final case class NABosComponent(date: String, status: Boolean, commitHash: String)

object NABosComponentJsonImplicits {
    implicit val naBosCompFmt = Json.format[NABosComponent]
    implicit val naBosCompWrites = Json.writes[NABosComponent]
    implicit val naBosCompReads = Json.reads[NABosComponent]
}
