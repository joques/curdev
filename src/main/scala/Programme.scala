import play.api.libs.json.Json

case class Programme(faculty: Int, department: Int, title: String, lvel: Int, isPreProgramme: Boolean, progComponent: Option[ProgrammeComponent], preProgComponent: Option[PreProgrammeComponent])

object ProgrammeJsonImplicits {
    implicit val preProgrammeComponentFormat: Format[PreProgrammeComponent] =  PreProgrammeComponentJsonImplicits.preProgCompFmt
    implicit val programmeFormat: Format[ProgrammeComponent] =  ProgrammeComponentJsonImplicits.progCompFmt

    implicit val prgFmt = Json.format[Programme]
    implicit val prgWrites = Json.writes[Programme]
    implicit val prgReads = Json.reads[Programme]
}
