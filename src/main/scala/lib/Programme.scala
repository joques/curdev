package yester.lib

import com.couchbase.client.scala.implicits.Codec
import play.api.libs.json.{Json, Format, Writes}

import ProgrammeComponent._
import PreProgrammeComponent._

final case class Programme(faculty: Int, department: Int, name: String, level: Int, isPreProgramme: Boolean, progComponent: Option[ProgrammeComponent], preProgComponent: Option[PreProgrammeComponent])

object Programme {
	implicit val progCodec: Codec[Programme] = Codec.codec[Programme]

	implicit val prgFmt = Json.format[Programme]
    implicit val prgWrites = Json.writes[Programme]
    implicit val prgReads = Json.reads[Programme]
}