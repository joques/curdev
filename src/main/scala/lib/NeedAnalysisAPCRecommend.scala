package yester.lib

import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec

final case class NeedAnalysisAPCRecommend(devCode: String, date: String, status: String, commitHash: Option[String])

object NeedAnalysisAPCRecommend {
	implicit val nasCodec: Codec[NeedAnalysisAPCRecommend] = Codec.codec[NeedAnalysisAPCRecommend]

	implicit val needAnaSRFmt = Json.format[NeedAnalysisAPCRecommend]
    implicit val needAnaSRWrites = Json.writes[NeedAnalysisAPCRecommend]
    implicit val needAnaSRReads = Json.reads[NeedAnalysisAPCRecommend]
}
