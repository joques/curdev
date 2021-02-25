package yester.lib

import com.couchbase.client.scala.implicits.Codec

// import play.api.libs.json.Json

final case class PreProgrammeComponent(devCode: String, initiator: String)

object PreProgrammeComponent {
	implicit val pProgCodec: Codec[PreProgrammeComponent] = Codec.codec[PreProgrammeComponent]
}


// object PreProgrammeComponentJsonImplicits {
//     implicit val preProgCompFmt = Json.format[PreProgrammeComponent]
//     implicit val preProgCompWrites = Json.writes[PreProgrammeComponent]
//     implicit val preProgCompReads = Json.reads[PreProgrammeComponent]
// }
