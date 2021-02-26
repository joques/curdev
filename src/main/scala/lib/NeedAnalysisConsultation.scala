package yester.lib

import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec

final case class NeedAnalysisConsultation(date: String, organization: String, devCode: String, commitHash: Option[String])

object NeedAnalysisConsultation {
	implicit val nasCodec: Codec[NeedAnalysisConsultation] = Codec.codec[NeedAnalysisConsultation]

	implicit val needAnaConsFmt = Json.format[NeedAnalysisConsultation]
    implicit val needAnaConsWrites = Json.writes[NeedAnalysisConsultation]
    implicit val needAnaConsReads = Json.reads[NeedAnalysisConsultation]
}