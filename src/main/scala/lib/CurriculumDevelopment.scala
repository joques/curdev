package yester.lib

import play.api.libs.json.{Json, Format}

final case class CurriculumDevelopment(pacMembers: Option[List[SingleCommitteeMember]], cdcMembers: Option[List[SingleCommitteeMember]], submissionDate: Option[String], decision: Option[String])

object CurriculumDevelopmentJsonImplicits {
    implicit val singleComMemberFmt: Format[SingleCommitteeMember] = SingleCommitteeMemberJsonImplicits.singleComFmt

    implicit val cdFmt = Json.format[CurriculumDevelopment]
    implicit val cdWrites = Json.writes[CurriculumDevelopment]
    implicit val cdReads = Json.reads[CurriculumDevelopment]
}
