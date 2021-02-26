package yester.message.response

import com.couchbase.client.scala.implicits.Codec
import play.api.libs.json.{Reads, Json, Format}

import yester.lib.UserWithPreProgramme

final case class UserWithPreProgrammeResponseMessage(messageId: String, operationError: Option[String], operationResult: Option[UserWithPreProgramme]) extends ResponseMessage[UserWithPreProgramme](messageId, operationError, operationResult)

object UserWithPreProgrammeResponseMessage {
	implicit val codec: Codec[UserWithPreProgrammeResponseMessage] = Codec.codec[UserWithPreProgrammeResponseMessage]

    implicit val uwPPResponseMessageFmt = Json.format[UserWithPreProgrammeResponseMessage]
    implicit val uwPPResponseMessageWrites = Json.writes[UserWithPreProgrammeResponseMessage]
    implicit val uwPPResponseMessageReads = Json.reads[UserWithPreProgrammeResponseMessage]
}