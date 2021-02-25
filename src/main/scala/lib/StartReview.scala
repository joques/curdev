package yester.lib

// import play.api.libs.json.Json

import com.couchbase.client.scala.implicits.Codec

final case class StartReview(devCode: String, reviewDate: String)

object StartReview {
    implicit val sreviewCodec: Codec[StartReview] = Codec.codec[StartReview]
}

// object StartReviewJsonImplicits {
//     implicit val stReviewFmt = Json.format[StartReview]
//     implicit val stReviewWrites = Json.writes[StartReview]
//     implicit val stReviewReads = Json.reads[StartReview]
// }
