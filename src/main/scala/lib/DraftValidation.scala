package yester.lib

import play.api.libs.json.Json

final case class DraftValidation(devCode: String, decision: String)

object DraftValidationJsonImplicits {
    implicit val draftValFmt = Json.format[DraftValidation]
    implicit val draftValWrites = Json.writes[DraftValidation]
    implicit val draftValReads = Json.reads[DraftValidation]
}
