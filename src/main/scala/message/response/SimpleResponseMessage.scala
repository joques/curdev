package yester.message.response


import com.couchbase.client.scala.implicits.Codec
// import play.api.libs.json.{Reads, Json, Format}

final case class SimpleResponseMessage(messageId: String, operationError: Option[String], operationResult: Option[String]) extends ResponseMessage[String](messageId, operationError, operationResult)

object SimpleResponseMessage {
	implicit val codec: Codec[SimpleResponseMessage] = Codec.codec[SimpleResponseMessage]
}

// object SimpleResponseMessageJsonImplicits {
//     implicit val simpleResponseMessageFmt = Json.format[SimpleResponseMessage]
//     implicit val simpleResponseMessageWrites = Json.writes[SimpleResponseMessage]
//     implicit val simpleResponseMessageReads = Json.reads[SimpleResponseMessage]
// }
