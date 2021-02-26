package yester.lib

import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec

final case class NAConsultationComponent(date: String, organization: String, commitHash: Option[String])

object NAConsultationComponent {
	implicit val codec: Codec[NAConsultationComponent] = Codec.codec[NAConsultationComponent]

	implicit val naConsCompFmt = Json.format[NAConsultationComponent]
    implicit val naConsCompWrites = Json.writes[NAConsultationComponent]
    implicit val naConsCompReads = Json.reads[NAConsultationComponent]
}