package yester.lib

import play.api.libs.json.Json

final case class CommitteeMembers(devCode: String, members: List[String], action: String)

object CommitteeMembersJsonImplicits {
    implicit val cmtMembersFmt = Json.format[CommitteeMembers]
    implicit val cmtMembersWrites = Json.writes[CommitteeMembers]
    implicit val cmtMembersReads = Json.reads[CommitteeMembers]
}
