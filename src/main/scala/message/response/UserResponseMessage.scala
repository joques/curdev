package yester.message.response

import play.api.libs.json.{Reads, Json, Format}
import com.couchbase.client.scala.implicits.Codec
import yester.lib.User

final case class UserResponseMessage(messageId: String, operationError: Option[String], operationResult: Option[User]) extends ResponseMessage[User](messageId, operationError, operationResult)

object UserResponseMessage {
	implicit val codec: Codec[UserResponseMessage] = Codec.codec[UserResponseMessage]

	implicit val userResponseMessageFmt = Json.format[UserResponseMessage]
    implicit val userResponseMessageWrites = Json.writes[UserResponseMessage]
    implicit val userResponseMessageReads = Json.reads[UserResponseMessage]
}