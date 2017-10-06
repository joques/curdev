package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{PACommitteeMembers, PACommitteeMembersJsonImplicits}

final case class CurriculumDevelopmentAppointPACRequestMessage(messageId: String, content: PACommitteeMembers) extends ComplexRequestMessage[PACommitteeMembers](messageId, content)

object CurriculumDevelopmentAppointPACRequestMessageJsonImplicits {
    implicit val paComMembFormat: Format[PACommitteeMembers] =  PACommitteeMembersJsonImplicits.paCmtMembersFmt

    implicit val cdpacmembRequestMessageFmt = Json.format[CurriculumDevelopmentAppointPACRequestMessage]
    implicit val cdpacmembRequestMessageeWrites = Json.writes[CurriculumDevelopmentAppointPACRequestMessage]
    implicit val cdpacmembRequestMessageReads = Json.reads[CurriculumDevelopmentAppointPACRequestMessage]
}
