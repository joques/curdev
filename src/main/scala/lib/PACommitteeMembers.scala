package yester.lib


import com.couchbase.client.scala.implicits.Codec

// import play.api.libs.json.{Json, Format}

final case class PACommitteeMembers(devCode: String, members: List[SingleCommitteeMember])

object PACommitteeMembers {
	implicit val pacMembCodec: Codec[PACommitteeMembers] = Codec.codec[PACommitteeMembers]
}

// object PACommitteeMembersJsonImplicits {
//     implicit val singleCommitteeMemberFormat: Format[SingleCommitteeMember] = SingleCommitteeMemberJsonImplicits.singleComFmt

//     implicit val paCmtMembersFmt = Json.format[PACommitteeMembers]
//     implicit val paCmtMembersWrites = Json.writes[PACommitteeMembers]
//     implicit val paCmtMembersReads = Json.reads[PACommitteeMembers]
// }
