package yester.message.request

import com.couchbase.client.scala.implicits.Codec
import play.api.libs.json.{Reads, Json, Format}

import yester.lib.PACommitteeMembers

final case class CurriculumDevelopmentAppointPACRequestMessage(messageId: String, content: PACommitteeMembers) extends ComplexRequestMessage[PACommitteeMembers](messageId, content)

object CurriculumDevelopmentAppointPACRequestMessage {
	implicit val codec: Codec[CurriculumDevelopmentAppointPACRequestMessage] = Codec.codec[CurriculumDevelopmentAppointPACRequestMessage]

	implicit val cdpacmembRequestMessageFmt = Json.format[CurriculumDevelopmentAppointPACRequestMessage]
    implicit val cdpacmembRequestMessageeWrites = Json.writes[CurriculumDevelopmentAppointPACRequestMessage]
    implicit val cdpacmembRequestMessageReads = Json.reads[CurriculumDevelopmentAppointPACRequestMessage]
}