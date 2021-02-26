package yester.lib

import com.couchbase.client.scala.implicits.Codec
import play.api.libs.json.{Json, Format, Writes}

import SingleCommitteeMember._

final case class CurriculumDevelopment(pacMembers: Option[List[SingleCommitteeMember]], cdcMembers: Option[List[SingleCommitteeMember]], submissionDate: Option[String], decision: Option[String])

object CurriculumDevelopment {
	implicit val codec: Codec[CurriculumDevelopment] = Codec.codec[CurriculumDevelopment]

	implicit val cdFmt = Json.format[CurriculumDevelopment]
    implicit val cdWrites = Json.writes[CurriculumDevelopment]
    implicit val cdReads = Json.reads[CurriculumDevelopment]
}