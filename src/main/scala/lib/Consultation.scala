package yester.lib

// import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec


final case class Consultation(devCode: String, consDate: String)

object Consultation {
	implicit val codec: Codec[Consultation] = Codec.codec[Consultation]
}

// object ConsultationJsonImplicits {
//     implicit val consFmt = Json.format[Consultation]
//     implicit val consWrites = Json.writes[Consultation]
//     implicit val consReads = Json.reads[Consultation]
// }
