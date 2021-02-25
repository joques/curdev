package yester.lib

// import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec

final case class CurriculumReview(devCode: String, code: String, initiator: String)

object CurriculumReview {
	implicit val codec: Codec[CurriculumReview] = Codec.codec[CurriculumReview]
}

// object CurriculumReviewJsonImplicits {
//     implicit val crvFmt = Json.format[CurriculumReview]
//     implicit val crvWrites = Json.writes[CurriculumReview]
//     implicit val crvReads = Json.reads[CurriculumReview]
// }
