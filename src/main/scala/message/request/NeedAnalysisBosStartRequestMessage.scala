package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{NeedAnalysisBosStart, NeedAnalysisBosStartJsonImplicits}

final case class NeedAnalysisBosStartRequestMessage(messageId: String, content: NeedAnalysisBosStart) extends ComplexRequestMessage[NeedAnalysisBosStart](messageId, content)

object NeedAnalysisBosStartRequestMessageJsonImplicits {
    implicit val needAnaBSFormat: Format[NeedAnalysisBosStart] =  NeedAnalysisBosStartJsonImplicits.needAnaBSFmt

    implicit val needAnaBSRequestMessageFmt = Json.format[NeedAnalysisBosStartRequestMessage]
    implicit val needAnaBSRequestMessageeWrites = Json.writes[NeedAnalysisBosStartRequestMessage]
    implicit val needAnaBSRequestMessageReads = Json.reads[NeedAnalysisBosStartRequestMessage]
}
