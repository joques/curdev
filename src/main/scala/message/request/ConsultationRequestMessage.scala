package yester.message.request


import com.couchbase.client.scala.implicits.Codec
// import play.api.libs.json.{Reads, Json, Format}

import yester.lib.Consultation

final case class ConsultationRequestMessage(messageId: String, content: Consultation) extends ComplexRequestMessage[Consultation](messageId, content)

object ConsultationRequestMessage {
	implicit val codec: Codec[ConsultationRequestMessage] = Codec.codec[ConsultationRequestMessage]
}

// object ConsultationRequestMessageJsonImplicits {
//     implicit val consFormat: Format[Consultation] =  ConsultationJsonImplicits.consFmt

//     implicit val consRequestMessageFmt = Json.format[ConsultationRequestMessage]
//     implicit val consRequestMessageeWrites = Json.writes[ConsultationRequestMessage]
//     implicit val consRequestMessageReads = Json.reads[ConsultationRequestMessage]
// }
