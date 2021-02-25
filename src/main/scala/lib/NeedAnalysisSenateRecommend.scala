package yester.lib

// import play.api.libs.json.Json

import com.couchbase.client.scala.implicits.Codec

final case class NeedAnalysisSenateRecommend(devCode: String, date: String, status: String, madeBy: String, commitHash: Option[String])

object NeedAnalysisSenateRecommend {
	implicit val nasCodec: Codec[NeedAnalysisSenateRecommend] = Codec.codec[NeedAnalysisSenateRecommend]
}

// object NeedAnalysisSenateRecommendJsonImplicits {
//     implicit val needAnaSRFmt = Json.format[NeedAnalysisSenateRecommend]
//     implicit val needAnaSRWrites = Json.writes[NeedAnalysisSenateRecommend]
//     implicit val needAnaSRReads = Json.reads[NeedAnalysisSenateRecommend]
// }
