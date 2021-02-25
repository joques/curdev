package yester.lib

// import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec

final case class Endorsement(devCode: String, endorsementDate: String, decision: String)

object Endorsement {
	implicit val codec: Codec[Endorsement] = Codec.codec[Endorsement]
}

// object EndorsementJsonImplicits {
//     implicit val endFmt = Json.format[Endorsement]
//     implicit val endWrites = Json.writes[Endorsement]
//     implicit val endReads = Json.reads[Endorsement]
// }
