package yester.lib

import akka.util.ByteString
import play.api.libs.json.{Json, Format, Writes}
import org.reactivecouchbase.rs.scaladsl.json.{JsonReads, JsonWrites, JsonFormat, JsonSuccess, JsonResult, JsonError}

final case class Programme(faculty: Int, department: Int, name: String, level: Int, isPreProgramme: Boolean, progComponent: Option[ProgrammeComponent], preProgComponent: Option[PreProgrammeComponent])

object ProgrammeJsonImplicits {
    implicit val preProgrammeComponentFormat: Format[PreProgrammeComponent] =  PreProgrammeComponentJsonImplicits.preProgCompFmt
    implicit val programmeFormat: Format[ProgrammeComponent] =  ProgrammeComponentJsonImplicits.progCompFmt

    implicit val prgFmt = Json.format[Programme]
    implicit val prgWrites = Json.writes[Programme]
    implicit val prgReads = Json.reads[Programme]
	
	
	def convertJsonFormat[Programme](modelFormat: Format[Programme]): JsonFormat[Programme] =
    JsonFormat[Programme](
      JsonReads[Programme](
        bs =>
          modelFormat
            .reads(Json.parse(bs.utf8String))
            .map(result => JsonSuccess(result))
            .getOrElse[JsonResult[Programme]](JsonError())
      ),
      JsonWrites[Programme](jsv => ByteString(Json.stringify(modelFormat.writes(jsv))))
    )
	
	implicit val progJsonFormat: JsonFormat[Programme] = convertJsonFormat(prgFmt)
}
