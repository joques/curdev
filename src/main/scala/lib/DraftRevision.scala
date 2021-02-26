package yester.lib

import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec

final case class DraftRevision(devCode: String)

object DraftRevision {
	implicit val codec: Codec[DraftRevision] = Codec.codec[DraftRevision]

	implicit val draftRevFmt = Json.format[DraftRevision]
    implicit val draftRevWrites = Json.writes[DraftRevision]
    implicit val draftRevReads = Json.reads[DraftRevision]
}