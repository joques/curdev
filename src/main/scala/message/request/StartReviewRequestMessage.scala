package yester.message.request

// import play.api.libs.json.{Reads, Json, Format}

import com.couchbase.client.scala.implicits.Codec

import yester.lib.StartReview

final case class StartReviewRequestMessage(messageId: String, content: StartReview) extends ComplexRequestMessage[StartReview](messageId, content)

object StartReviewRequestMessage {
	implicit val codec: Codec[StartReviewRequestMessage] = Codec.codec[StartReviewRequestMessage]
}


// object StartReviewRequestMessageJsonImplicits {
//     implicit val stReviewFormat: Format[StartReview] =  StartReviewJsonImplicits.stReviewFmt

//     implicit val sRevRequestMessageFmt = Json.format[StartReviewRequestMessage]
//     implicit val sRevRequestMessageeWrites = Json.writes[StartReviewRequestMessage]
//     implicit val sRevRequestMessageReads = Json.reads[StartReviewRequestMessage]
// }
