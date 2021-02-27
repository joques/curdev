package yester.message.response


import com.couchbase.client.scala.implicits.Codec
import play.api.libs.json.{Reads, Json, Format}
import yester.lib.Summary

final case class SummaryResponseMessage(messageId: String, operationError: Option[String], operationResult: Option[Summary]) extends ResponseMessage[Summary](messageId, operationError, operationResult)

object SummaryResponseMessage {
	implicit val codec: Codec[SummaryResponseMessage] = Codec.codec[SummaryResponseMessage]

	implicit val summaryResponseMessageFmt = Json.format[SummaryResponseMessage]
    implicit val summaryResponseMessageWrites = Json.writes[SummaryResponseMessage]
    implicit val summaryResponseMessageReads = Json.reads[SummaryResponseMessage]
}