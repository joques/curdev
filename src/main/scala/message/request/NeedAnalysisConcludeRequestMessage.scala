package yester.message.request

// import play.api.libs.json.{Reads, Json, Format}

import yester.lib.NeedAnalysisConclude

final case class NeedAnalysisConcludeRequestMessage(messageId: String, content: NeedAnalysisConclude) extends ComplexRequestMessage[NeedAnalysisConclude](messageId, content)

// object NeedAnalysisConcludeRequestMessageJsonImplicits {
//     implicit val needAnaConclFormat: Format[NeedAnalysisConclude] =  NeedAnalysisConcludeJsonImplicits.needAnaConclFmt

//     implicit val needAnaConclRequestMessageFmt = Json.format[NeedAnalysisConcludeRequestMessage]
//     implicit val needAnaConclRequestMessageeWrites = Json.writes[NeedAnalysisConcludeRequestMessage]
//     implicit val needAnaConclRequestMessageReads = Json.reads[NeedAnalysisConcludeRequestMessage]
// }
