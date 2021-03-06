package yester.lib

import play.api.libs.json.{Json, Format}
import com.couchbase.client.scala.implicits.Codec

import Programme._

final case class Summary(inProgress: Option[Seq[Programme]], dueForReview: Option[Seq[Programme]], recentlyApproved: Option[Seq[Programme]])

object Summary {
    implicit val summaryCodec: Codec[Summary] = Codec.codec[Summary]

    implicit val summaryFmt = Json.format[Summary]
    implicit val summaryWrites = Json.writes[Summary]
    implicit val summaryReads = Json.reads[Summary]
}