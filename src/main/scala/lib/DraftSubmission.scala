package yester.lib

import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec

final case class DraftSubmission(devCode: String, submissionDate: String)

object DraftSubmission {
	implicit val codec: Codec[DraftSubmission] = Codec.codec[DraftSubmission]

	implicit val draftSubFmt = Json.format[DraftSubmission]
    implicit val draftSubWrites = Json.writes[DraftSubmission]
    implicit val draftSubReads = Json.reads[DraftSubmission]
}