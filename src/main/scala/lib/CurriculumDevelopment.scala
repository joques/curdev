package yester.lib

import play.api.libs.json.{Json, Format}
import org.reactivecouchbase.rs.scaladsl.json.{JsonReads, JsonWrites, JsonFormat, JsonSuccess}

final case class CurriculumDevelopment(pacMembers: Option[List[SingleCommitteeMember]], cdcMembers: Option[List[SingleCommitteeMember]], submissionDate: Option[String], decision: Option[String])

object CurriculumDevelopmentJsonImplicits {
    implicit val singleComMemberFmt: Format[SingleCommitteeMember] = SingleCommitteeMemberJsonImplicits.singleComFmt

    implicit val cdFmt = Json.format[CurriculumDevelopment]
    implicit val cdWrites = Json.writes[CurriculumDevelopment]
    implicit val cdReads = Json.reads[CurriculumDevelopment]
	
	implicit val cdJsonReads: JsonReads[CurriculumDevelopment] = JsonReads(bs => JsonSuccess(bs)
	implicit val cdJsonWrites: JsonWrites[CurriculumDevelopment] = JsonWrites(bs => bs)
	implicit val defaultCDFormat: JsonFormat[CurriculumDevelopment] = JsonFormat(read, write)
}
