package yester.lib

// import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec

final case class DraftValidation(devCode: String, decision: String)

object DraftValidation {
	implicit val codec: Codec[DraftValidation] = Codec.codec[DraftValidation]
}

// object DraftValidationJsonImplicits {
//     implicit val draftValFmt = Json.format[DraftValidation]
//     implicit val draftValWrites = Json.writes[DraftValidation]
//     implicit val draftValReads = Json.reads[DraftValidation]
// }
