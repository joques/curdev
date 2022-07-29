package yester.lib

import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec

final case class NAAPCComponent(date: String, status: String, commitHash: Option[String])

object NAAPCComponent {
	implicit val codec: Codec[NAAPCComponent] = Codec.codec[NAAPCComponent]

	implicit val naAPCCompFmt = Json.format[NAAPCComponent]
    implicit val naAPCCompWrites = Json.writes[NAAPCComponent]
    implicit val naAPCCompReads = Json.reads[NAAPCComponent]
}
