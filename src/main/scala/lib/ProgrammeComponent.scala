package yester.lib

// import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec


final case class ProgrammeComponent(approvedOn: String, nextReview: String, history: List[String], code: Option[String])

object ProgrammeComponent {
	implicit val pComCodec: Codec[ProgrammeComponent] = Codec.codec[ProgrammeComponent]
}

// object ProgrammeComponentJsonImplicits {
//     implicit val progCompFmt = Json.format[ProgrammeComponent]
//     implicit val progCompWrites = Json.writes[ProgrammeComponent]
//     implicit val progCompReads = Json.reads[ProgrammeComponent]
// }
