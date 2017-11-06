package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{Endorsement, EndorsementJsonImplicits}

final case class EndorsementRequestMessage(messageId: String, content: Endorsement) extends ComplexRequestMessage[Endorsement](messageId, content)

object EndorsementRequestMessageJsonImplicits {
    implicit val endFormat: Format[Endorsement] =  EndorsementJsonImplicits.endFmt

    implicit val endRequestMessageFmt = Json.format[EndorsementRequestMessage]
    implicit val endRequestMessageeWrites = Json.writes[EndorsementRequestMessage]
    implicit val endRequestMessageReads = Json.reads[EndorsementRequestMessage]
}
