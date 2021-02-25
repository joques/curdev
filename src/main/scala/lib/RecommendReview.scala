package yester.lib

// import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec

final case class RecommendReview(devCode: String, reviewUnit: String, decision: String)

object RecommendReview {
	implicit val rReviewCodec: Codec[RecommendReview] = Codec.codec[RecommendReview]
}

// object RecommendReviewJsonImplicits {
//     implicit val rcReviewFmt = Json.format[RecommendReview]
//     implicit val rcReviewWrites = Json.writes[RecommendReview]
//     implicit val rcReviewReads = Json.reads[RecommendReview]
// }
