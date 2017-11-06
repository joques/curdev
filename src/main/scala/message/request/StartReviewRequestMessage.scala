package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{StartReview, StartReviewJsonImplicits}

final case class StartReviewRequestMessage(messageId: String, content: StartReview) extends ComplexRequestMessage[StartReview](messageId, content)

object FinalDraftRequestMessageJsonImplicits {
    implicit val stReviewFormat: Format[StartReview] =  StartReviewJsonImplicits.stReviewFmt

    implicit val sRevRequestMessageFmt = Json.format[StartReviewRequestMessage]
    implicit val sRevRequestMessageeWrites = Json.writes[StartReviewRequestMessage]
    implicit val sRevRequestMessageReads = Json.reads[StartReviewRequestMessage]
}
