package yester.lib

// import play.api.libs.json.Json

import com.couchbase.client.scala.implicits.Codec

final case class NeedAnalysisSurvey(devCode: String, commitHash: Option[String])

object NeedAnalysisSurvey {
	implicit val pacMembCodec: Codec[NeedAnalysisSurvey] = Codec.codec[NeedAnalysisSurvey]
}


// object NeedAnalysisSurveyJsonImplicits {
//     implicit val needAnaSurvFmt = Json.format[NeedAnalysisSurvey]
//     implicit val needAnaSurvWrites = Json.writes[NeedAnalysisSurvey]
//     implicit val needAnaSurvReads = Json.reads[NeedAnalysisSurvey]
// }
