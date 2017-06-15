package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{NeedAnalysisSurvey,NeedAnalysisSurveyJsonImplicits}

final case class NeedAnalysisSurveyRequestMessage(messageId: String, content: NeedAnalysisSurvey) extends ComplexRequestMessage[NeedAnalysisSurvey](messageId, content)

object NeedAnalysisSurveyRequestMessageJsonImplicits {
    implicit val needAnaSurvFormat: Format[NeedAnalysisSurvey] =  NeedAnalysisSurveyJsonImplicits.needAnaSurvFmt

    implicit val needAnaSurvRequestMessageFmt = Json.format[NeedAnalysisSurveyRequestMessage]
    implicit val needAnaSurvRequestMessageeWrites = Json.writes[NeedAnalysisSurveyRequestMessage]
    implicit val needAnaSurvRequestMessageReads = Json.reads[NeedAnalysisSurveyRequestMessage]
}
