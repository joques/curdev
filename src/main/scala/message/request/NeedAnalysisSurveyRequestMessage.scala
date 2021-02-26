package yester.message.request

// import play.api.libs.json.{Reads, Json, Format}

import com.couchbase.client.scala.implicits.Codec
import yester.lib.NeedAnalysisSurvey

final case class NeedAnalysisSurveyRequestMessage(messageId: String, content: NeedAnalysisSurvey) extends ComplexRequestMessage[NeedAnalysisSurvey](messageId, content)

object NeedAnalysisSurveyRequestMessage {
	implicit val codec: Codec[NeedAnalysisSurveyRequestMessage] = Codec.codec[NeedAnalysisSurveyRequestMessage]
}

// object NeedAnalysisSurveyRequestMessageJsonImplicits {
//     implicit val needAnaSurvFormat: Format[NeedAnalysisSurvey] =  NeedAnalysisSurveyJsonImplicits.needAnaSurvFmt

//     implicit val needAnaSurvRequestMessageFmt = Json.format[NeedAnalysisSurveyRequestMessage]
//     implicit val needAnaSurvRequestMessageeWrites = Json.writes[NeedAnalysisSurveyRequestMessage]
//     implicit val needAnaSurvRequestMessageReads = Json.reads[NeedAnalysisSurveyRequestMessage]
// }
