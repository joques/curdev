package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{FinalDraft, FinalDraftJsonImplicits}

final case class FinalDraftRequestMessage(messageId: String, content: FinalDraft) extends ComplexRequestMessage[FinalDraft](messageId, content)

object FinalDraftRequestMessageJsonImplicits {
    implicit val fDraftFormat: Format[FinalDraft] =  FinalDraftJsonImplicits.fdraftFmt

    implicit val fdRequestMessageFmt = Json.format[FinalDraftRequestMessage]
    implicit val fdRequestMessageeWrites = Json.writes[FinalDraftRequestMessage]
    implicit val fdRequestMessageReads = Json.reads[FinalDraftRequestMessage]
}
