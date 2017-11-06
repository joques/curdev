package yester.lib

import play.api.libs.json.Json

final case class FinalDraft(devCode: String)

object FinalDraftJsonImplicits {
    implicit val fdraftFmt = Json.format[FinalDraft]
    implicit val fdrafWrites = Json.writes[FinalDraft]
    implicit val fdrafReads = Json.reads[FinalDraft]
}
