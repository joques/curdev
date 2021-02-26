package yester.lib

import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec

final case class NASurveyComponent(commitHash: Option[String])

object NASurveyComponent {
	implicit val codec: Codec[NASurveyComponent] = Codec.codec[NASurveyComponent]

	implicit val naSurvCompFmt = Json.format[NASurveyComponent]
    implicit val naSurvCompWrites = Json.writes[NASurveyComponent]
    implicit val naSurvCompReads = Json.reads[NASurveyComponent]
}