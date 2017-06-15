package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{CurriculumDevelopmentAuthorization, CurriculumDevelopmentAuthorizationJsonImplicits}

final case class CurriculumDevelopmentAuthorizationRequestMessage(messageId: String, content: CurriculumDevelopmentAuthorization) extends ComplexRequestMessage[CurriculumDevelopmentAuthorization](messageId, content)

object CurriculumDevelopmentAuthorizationRequestMessageJsonImplicits {
    implicit val cdaFormat: Format[CurriculumDevelopmentAuthorization] =  CurriculumDevelopmentAuthorizationJsonImplicits.cdaFmt

    implicit val cdaRequestMessageFmt = Json.format[CurriculumDevelopmentAuthorizationRequestMessage]
    implicit val cdaRequestMessageeWrites = Json.writes[CurriculumDevelopmentAuthorizationRequestMessage]
    implicit val cdaRequestMessageReads = Json.reads[CurriculumDevelopmentAuthorizationRequestMessage]
}
