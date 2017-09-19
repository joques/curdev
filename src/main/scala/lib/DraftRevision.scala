package yester.lib

import play.api.libs.json.Json

final case class DraftRevision(devCode: String)

object DraftRevisionJsonImplicits {
    implicit val draftRevFmt = Json.format[DraftRevision]
    implicit val draftRevWrites = Json.writes[DraftRevision]
    implicit val draftRevReads = Json.reads[DraftRevision]
}
