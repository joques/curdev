package yester.lib

import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec

final case class NASenateComponent(date: String, status: String, commitHash: Option[String])

object NASenateComponent {
	implicit val codec: Codec[NASenateComponent] = Codec.codec[NASenateComponent]

	implicit val naSenateCompFmt = Json.format[NASenateComponent]
    implicit val naSenateCompWrites = Json.writes[NASenateComponent]
    implicit val naSenateCompReads = Json.reads[NASenateComponent]
}