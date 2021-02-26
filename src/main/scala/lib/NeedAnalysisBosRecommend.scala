package yester.lib

import play.api.libs.json.Json

import com.couchbase.client.scala.implicits.Codec

final case class NeedAnalysisBosRecommend(devCode: String, date: String, status: Boolean, commitHash:Option[String])

object NeedAnalysisBosRecommend {
	implicit val nacCodec: Codec[NeedAnalysisBosStart] = Codec.codec[NeedAnalysisBosStart]

	implicit val needAnaBRFmt = Json.format[NeedAnalysisBosRecommend]
    implicit val needAnaBRWrites = Json.writes[NeedAnalysisBosRecommend]
    implicit val needAnaBRReads = Json.reads[NeedAnalysisBosRecommend]
}