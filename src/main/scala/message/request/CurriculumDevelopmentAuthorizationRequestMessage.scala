package yester.message.request

import com.couchbase.client.scala.implicits.Codec
import play.api.libs.json.{Reads, Json, Format}

import yester.lib.CurriculumDevelopmentAuthorization

final case class CurriculumDevelopmentAuthorizationRequestMessage(messageId: String, content: CurriculumDevelopmentAuthorization) extends ComplexRequestMessage[CurriculumDevelopmentAuthorization](messageId, content)

object CurriculumDevelopmentAuthorizationRequestMessage {
	implicit val codec: Codec[CurriculumDevelopmentAuthorizationRequestMessage] = Codec.codec[CurriculumDevelopmentAuthorizationRequestMessage]

	implicit val cdaRequestMessageFmt = Json.format[CurriculumDevelopmentAuthorizationRequestMessage]
    implicit val cdaRequestMessageeWrites = Json.writes[CurriculumDevelopmentAuthorizationRequestMessage]
    implicit val cdaRequestMessageReads = Json.reads[CurriculumDevelopmentAuthorizationRequestMessage]
}