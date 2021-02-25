package yester.lib

// import play.api.libs.json.Json

import com.couchbase.client.scala.implicits.Codec

final case class NeedAnalysisConclude(devCode: String, decision: String, commitHash: Option[String])

object NeedAnalysisConclude {
	implicit val nacCodec: Codec[NeedAnalysisConclude] = Codec.codec[NeedAnalysisConclude]
}

// object NeedAnalysisConcludeJsonImplicits {
//     implicit val needAnaConclFmt = Json.format[NeedAnalysisConclude]
//     implicit val needAnaConclWrites = Json.writes[NeedAnalysisConclude]
//     implicit val needAnaConclReads = Json.reads[NeedAnalysisConclude]
// }
