package yester.lib

import com.couchbase.client.scala.implicits.Codec
import play.api.libs.json.Json

final case class CurriculumDevelopmentAuthorization(devCode: String, date: String, action: String)

object CurriculumDevelopmentAuthorization {
	implicit val codec: Codec[CurriculumDevelopmentAuthorization] = Codec.codec[CurriculumDevelopmentAuthorization]

	implicit val cdaFmt = Json.format[CurriculumDevelopmentAuthorization]
    implicit val cdaWrites = Json.writes[CurriculumDevelopmentAuthorization]
    implicit val cdaReads = Json.reads[CurriculumDevelopmentAuthorization]
}