import play.api.libs.json.{Reads, Json, Format}

final case class SummaryResponseMessage(messageId: String, operationError: Option[String], operationResult: Option[Summary]) extends ResponseMessage[Summary](messageId, operationError, operationResult)

object SummaryResponseMessageJsonImplicits {
    implicit val summaryFormat: Format[Summary] =  SummaryJsonImplicits.summaryFmt

    implicit val summaryResponseMessageFmt = Json.format[SummaryResponseMessage]
    implicit val summaryResponseMessageWrites = Json.writes[SummaryResponseMessage]
    implicit val summaryResponseMessageReads = Json.reads[SummaryResponseMessage]
}
