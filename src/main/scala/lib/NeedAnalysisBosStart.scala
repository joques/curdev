package yester.lib

import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec

final case class NeedAnalysisBosStart(devCode: String, date: String)

object NeedAnalysisBosStart {
	implicit val nacCodec: Codec[NeedAnalysisBosStart] = Codec.codec[NeedAnalysisBosStart]

	implicit val needAnaBSFmt = Json.format[NeedAnalysisBosStart]
    implicit val needAnaBSWrites = Json.writes[NeedAnalysisBosStart]
    implicit val needAnaBSReads = Json.reads[NeedAnalysisBosStart]
}