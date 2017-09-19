package yester.lib

import play.api.libs.json.{Json, Format}

final case class CurriculumDevelopment(pacMembers: Option[List[SinglePACCommitteeMember]], submissionDate: Option[String], validated: Boolean)

object CurriculumDevelopmentJsonImplicits {
    implicit val singlePACMemberFmt: Format[SinglePACCommitteeMember] = SinglePACCommitteeMemberJsonImplicits.singlePACComFmt

    implicit val cdFmt = Json.format[CurriculumDevelopment]
    implicit val cdWrites = Json.writes[CurriculumDevelopment]
    implicit val cdReads = Json.reads[CurriculumDevelopment]
}
