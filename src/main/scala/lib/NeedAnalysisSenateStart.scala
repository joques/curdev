package yester.lib

// import play.api.libs.json.Json

import com.couchbase.client.scala.implicits.Codec

final case class NeedAnalysisSenateStart(devCode: String, date: String)

object NeedAnalysisSenateStart {
	implicit val nasCodec: Codec[NeedAnalysisSenateStart] = Codec.codec[NeedAnalysisSenateStart]
}

// object NeedAnalysisSenateStartJsonImplicits {
//     implicit val needAnaSSFmt = Json.format[NeedAnalysisSenateStart]
//     implicit val needAnaSSWrites = Json.writes[NeedAnalysisSenateStart]
//     implicit val needAnaSSReads = Json.reads[NeedAnalysisSenateStart]
// }
