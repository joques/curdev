package yester.lib

import play.api.libs.json.Json

final case class DraftSubmission(devCode: String, submissionDate: String)

object DraftSubmissionJsonImplicits {
    implicit val draftSubFmt = Json.format[DraftSubmission]
    implicit val draftSubWrites = Json.writes[DraftSubmission]
    implicit val draftSubReads = Json.reads[DraftSubmission]
}
