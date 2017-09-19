package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{PACCommitteeMembers, PACCommitteeMembersJsonImplicits}

final case class CurriculumDevelopmentAppointPACRequestMessage(messageId: String, content: PACCommitteeMembers) extends ComplexRequestMessage[PACCommitteeMembers](messageId, content)

object CurriculumDevelopmentAppointPACRequestMessageJsonImplicits {
    implicit val pacComMembFormat: Format[PACCommitteeMembers] =  PACCommitteeMembersJsonImplicits.pacCmtMembersFmt

    implicit val cdpacmembRequestMessageFmt = Json.format[CurriculumDevelopmentAppointPACRequestMessage]
    implicit val cdpacmembRequestMessageeWrites = Json.writes[CurriculumDevelopmentAppointPACRequestMessage]
    implicit val cdpacmembRequestMessageReads = Json.reads[CurriculumDevelopmentAppointPACRequestMessage]
}
