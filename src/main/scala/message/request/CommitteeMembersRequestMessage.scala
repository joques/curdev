package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{CommitteeMembers, CommitteeMembersJsonImplicits}

final case class CommitteeMembersRequestMessage(messageId: String, content: CommitteeMembers) extends ComplexRequestMessage[CommitteeMembers](messageId, content)

object CommitteeMembersJsonImplicitsRequestMessageJsonImplicits {
    implicit val cmtMembersFormat: Format[CommitteeMembers] =  CommitteeMembersJsonImplicits.needAnaSurvFmt

    implicit val cmtMembersRequestMessageFmt = Json.format[CommitteeMembersRequestMessage]
    implicit val cmtMembersRequestMessageeWrites = Json.writes[CommitteeMembersRequestMessage]
    implicit val cmtMembersRequestMessageReads = Json.reads[CommitteeMembersRequestMessage]
}
