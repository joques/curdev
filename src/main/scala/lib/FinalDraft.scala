package yester.lib

// import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec

final case class FinalDraft(devCode: String)

object FinalDraft {
	implicit val codec: Codec[FinalDraft] = Codec.codec[FinalDraft]
}

// object FinalDraftJsonImplicits {
//     implicit val fdraftFmt = Json.format[FinalDraft]
//     implicit val fdrafWrites = Json.writes[FinalDraft]
//     implicit val fdrafReads = Json.reads[FinalDraft]
// }
