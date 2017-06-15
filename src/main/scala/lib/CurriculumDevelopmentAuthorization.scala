package yester.lib

import play.api.libs.json.Json

final case class CurriculumDevelopmentAuthorization(devCode: String, date: String, action: String)

object CurriculumDevelopmentAuthorizationJsonImplicits {
    implicit val cdaFmt = Json.format[CurriculumDevelopmentAuthorization]
    implicit val cdaWrites = Json.writes[CurriculumDevelopmentAuthorization]
    implicit val cdaReads = Json.reads[CurriculumDevelopmentAuthorization]
}
