import play.api.libs.json.{Reads, Json, Format}

case class ProgrammeRequestMessage(messageId: String, content: Programme) extends ComplexRequestMessage[Programme](messageId, content)

object ProgrammeRequestMessageJsonImplicits {
    implicit val programmeFormat: Format[Programme] =  ProgrammeJsonImplicits.prgFmt

    implicit val programmeRequestMessageFmt = Json.format[ProgrammeRequestMessage]
    implicit val programmeRequestMessageeWrites = Json.writes[ProgrammeRequestMessage]
    implicit val programmeRequestMessageReads = Json.reads[ProgrammeRequestMessage]
}
