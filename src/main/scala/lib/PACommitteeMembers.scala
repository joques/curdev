package yester.lib

import play.api.libs.json.{Json, Format}

final case class PACommitteeMembers(devCode: String, members: List[SingleCommitteeMember])

object PACommitteeMembersJsonImplicits {
    implicit val singlePACommitteeMemberFormat: Format[SinglePACommitteeMember] = SingleCommitteeMemberJsonImplicits.singleComFmt

    implicit val paCmtMembersFmt = Json.format[PACommitteeMembers]
    implicit val paCmtMembersWrites = Json.writes[PACommitteeMembers]
    implicit val paCmtMembersReads = Json.reads[PACommitteeMembers]
}
