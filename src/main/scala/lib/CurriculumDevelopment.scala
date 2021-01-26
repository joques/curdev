package yester.lib

import akka.util.ByteString
import play.api.libs.json.{Json, Format, Writes}
import org.reactivecouchbase.rs.scaladsl.json.{JsonReads, JsonWrites, JsonFormat, JsonSuccess, JsonResult, JsonError}

final case class CurriculumDevelopment(pacMembers: Option[List[SingleCommitteeMember]], cdcMembers: Option[List[SingleCommitteeMember]], submissionDate: Option[String], decision: Option[String])

object CurriculumDevelopmentJsonImplicits {
    implicit val singleComMemberFmt: Format[SingleCommitteeMember] = SingleCommitteeMemberJsonImplicits.singleComFmt

    implicit val cdFmt = Json.format[CurriculumDevelopment]
    implicit val cdWrites = Json.writes[CurriculumDevelopment]
    implicit val cdReads = Json.reads[CurriculumDevelopment]
	
	
	implicit def convertJsonFormat[CurriculumDevelopment](modelFormat: Format[CurriculumDevelopment]): JsonFormat[CurriculumDevelopment] =
    JsonFormat[CurriculumDevelopment](
      JsonReads[CurriculumDevelopment](
        bs =>
          modelFormat
            .reads(Json.parse(bs.utf8String))
            .map(result => JsonSuccess(result))
            .getOrElse[JsonResult[CurriculumDevelopment]](JsonError())
      ),
      JsonWrites[CurriculumDevelopment](jsv => ByteString(Json.stringify(modelFormat.writes(jsv))))
    )
	
	implicit val cdJsonFormat: JsonFormat[CurriculumDevelopment] = convertJsonFormat(cdFmt)
}
