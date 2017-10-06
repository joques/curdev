package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{CDCommitteeMembers, CDCommitteeMembersJsonImplicits}

final case class CurriculumDevelopmentAppointCDCRequestMessage(messageId: String, content: CDCommitteeMembers) extends ComplexRequestMessage[CDCommitteeMembers](messageId, content)

object CurriculumDevelopmentAppointCDCRequestMessageJsonImplicits {
    implicit val cdComMembFormat: Format[CDCommitteeMembers] =  CDCommitteeMembersJsonImplicits.cdCmtMembersFmt

    implicit val cdcmembRequestMessageFmt = Json.format[CurriculumDevelopmentAppointCDCRequestMessage]
    implicit val cdcmembRequestMessageeWrites = Json.writes[CurriculumDevelopmentAppointCDCRequestMessage]
    implicit val cdcmembRequestMessageReads = Json.reads[CurriculumDevelopmentAppointCDCRequestMessage]
}
