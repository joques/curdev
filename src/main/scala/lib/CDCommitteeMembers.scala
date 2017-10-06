package yester.lib

import play.api.libs.json.{Json, Format}

final case class CDCommitteeMembers(devCode: String, members: List[SingleCommitteeMember])

object CDCommitteeMembersJsonImplicits {
    implicit val singleCommitteeMemberFormat: Format[SingleCommitteeMember] = SingleCommitteeMemberJsonImplicits.singleComFmt

    implicit val cdCmtMembersFmt = Json.format[CDCommitteeMembers]
    implicit val cdCmtMembersWrites = Json.writes[CDCommitteeMembers]
    implicit val cdCmtMembersReads = Json.reads[CDCommitteeMembers]
}
