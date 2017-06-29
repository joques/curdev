package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{NeedAnalysisSenateStart, NeedAnalysisSenateStartJsonImplicits}

final case class NeedAnalysisSenateStartRequestMessage(messageId: String, content: NeedAnalysisSenateStart) extends ComplexRequestMessage[NeedAnalysisSenateStart](messageId, content)

object NeedAnalysisSenateStartRequestMessageJsonImplicits {
    implicit val needAnaSSFormat: Format[NeedAnalysisSenateStart] =  NeedAnalysisSenateStartJsonImplicits.needAnaSSFmt

    implicit val needAnaBSRequestMessageFmt = Json.format[NeedAnalysisSenateStartRequestMessage]
    implicit val needAnaBSRequestMessageeWrites = Json.writes[NeedAnalysisSenateStartRequestMessage]
    implicit val needAnaBSRequestMessageReads = Json.reads[NeedAnalysisSenateStartRequestMessage]
}
