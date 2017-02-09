import play.api.libs.json.{Reads, Json, Format}

case class PreProgrammeRequestMessage(messageId: String, content: PreProgramme) extends ComplexRequestMessage[PreProgramme](messageId, content)

object PreProgrammeRequestMessageJsonImplicits {
    implicit val preProgrammeFormat: Format[PreProgramme] =  PreProgrammeJsonImplicits.preProgFmt

    implicit val preProgrammeRequestMessageFmt = Json.format[PreProgramme]
    implicit val preProgrammeRequestMessageeWrites = Json.writes[PreProgramme]
    implicit val preProgrammeRequestMessageReads = Json.reads[PreProgramme]
}
