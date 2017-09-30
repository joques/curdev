package yester.lib

import play.api.libs.json.{Json, Format}

final case class PACCommitteeMembers(devCode: String, members: List[SinglePACCommitteeMember])

object PACCommitteeMembersJsonImplicits {
    implicit val singlePACCommitteeMemberFormat: Format[SinglePACCommitteeMember] = SinglePACCommitteeMemberJsonImplicits.singlePACComFmt

    implicit val pacCmtMembersFmt = Json.format[PACCommitteeMembers]
    implicit val pacCmtMembersWrites = Json.writes[PACCommitteeMembers]
    implicit val pacCmtMembersReads = Json.reads[PACCommitteeMembers]
}
