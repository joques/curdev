package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import com.couchbase.client.scala.implicits.Codec

import yester.lib.CommitteeMembers._
import yester.lib.CommitteeMembers

final case class CommitteeMembersRequestMessage(messageId: String, content: CommitteeMembers) extends ComplexRequestMessage[CommitteeMembers](messageId, content)

object CommitteeMembersRequestMessage {
	implicit val codec: Codec[CommitteeMembersRequestMessage] = Codec.codec[CommitteeMembersRequestMessage]

	// implicit val cMembersFormat: Format[CommitteeMembers] = CommitteeMembers.cmtMembersFmt

	implicit val cmtMembersRequestMessageFmt = Json.format[CommitteeMembersRequestMessage]
    implicit val cmtMembersRequestMessageeWrites = Json.writes[CommitteeMembersRequestMessage]
    implicit val cmtMembersRequestMessageReads = Json.reads[CommitteeMembersRequestMessage]
}