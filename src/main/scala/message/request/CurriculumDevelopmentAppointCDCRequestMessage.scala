package yester.message.request

import com.couchbase.client.scala.implicits.Codec
// import play.api.libs.json.{Reads, Json, Format}

import yester.lib.CDCommitteeMembers

final case class CurriculumDevelopmentAppointCDCRequestMessage(messageId: String, content: CDCommitteeMembers) extends ComplexRequestMessage[CDCommitteeMembers](messageId, content)

object CurriculumDevelopmentAppointCDCRequestMessage {
	implicit val codec: Codec[CurriculumDevelopmentAppointCDCRequestMessage] = Codec.codec[CurriculumDevelopmentAppointCDCRequestMessage]
}

// object CurriculumDevelopmentAppointCDCRequestMessageJsonImplicits {
//     implicit val cdComMembFormat: Format[CDCommitteeMembers] =  CDCommitteeMembersJsonImplicits.cdCmtMembersFmt

//     implicit val cdcmembRequestMessageFmt = Json.format[CurriculumDevelopmentAppointCDCRequestMessage]
//     implicit val cdcmembRequestMessageeWrites = Json.writes[CurriculumDevelopmentAppointCDCRequestMessage]
//     implicit val cdcmembRequestMessageReads = Json.reads[CurriculumDevelopmentAppointCDCRequestMessage]
// }
