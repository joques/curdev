package yester.lib

// import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec

final case class NABosComponent(date: String, status: Boolean, commitHash: Option[String])

object NABosComponent {
	implicit val codec: Codec[NABosComponent] = Codec.codec[NABosComponent]
}

// object NABosComponentJsonImplicits {
//     implicit val naBosCompFmt = Json.format[NABosComponent]
//     implicit val naBosCompWrites = Json.writes[NABosComponent]
//     implicit val naBosCompReads = Json.reads[NABosComponent]
// }
