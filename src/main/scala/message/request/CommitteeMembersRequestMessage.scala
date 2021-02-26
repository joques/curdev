package yester.message.request

// import play.api.libs.json.{Reads, Json, Format}

import com.couchbase.client.scala.implicits.Codec

import yester.lib.CommitteeMembers

final case class CommitteeMembersRequestMessage(messageId: String, content: CommitteeMembers) extends ComplexRequestMessage[CommitteeMembers](messageId, content)

object CommitteeMembersRequestMessage {
	implicit val codec: Codec[CommitteeMembersRequestMessage] = Codec.codec[CommitteeMembersRequestMessage]
}

// object CommitteeMembersJsonImplicitsRequestMessageJsonImplicits {
//     implicit val cmtMembersFormat: Format[CommitteeMembers] =  CommitteeMembersJsonImplicits.cmtMembersFmt

//     implicit val cmtMembersRequestMessageFmt = Json.format[CommitteeMembersRequestMessage]
//     implicit val cmtMembersRequestMessageeWrites = Json.writes[CommitteeMembersRequestMessage]
//     implicit val cmtMembersRequestMessageReads = Json.reads[CommitteeMembersRequestMessage]
// }
