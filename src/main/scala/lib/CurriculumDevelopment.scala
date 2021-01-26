package yester.lib

import play.api.libs.json.{Json, Format}
import org.reactivecouchbase.rs.scaladsl.json.{JsonReads, JsonWrites, JsonFormat, JsonSuccess}

final case class CurriculumDevelopment(pacMembers: Option[List[SingleCommitteeMember]], cdcMembers: Option[List[SingleCommitteeMember]], submissionDate: Option[String], decision: Option[String])

object CurriculumDevelopmentJsonImplicits {
    implicit val singleComMemberFmt: Format[SingleCommitteeMember] = SingleCommitteeMemberJsonImplicits.singleComFmt

    implicit val cdFmt = Json.format[CurriculumDevelopment]
    implicit val cdWrites = Json.writes[CurriculumDevelopment]
    implicit val cdReads = Json.reads[CurriculumDevelopment]
		
	val cdJsonReads: JsonReads[CurriculumDevelopment]   = JsonReads(bs => JsonSuccess(Json.parse(bs.utf8String)))
  	val cdJsonWrites: JsonWrites[CurriculumDevelopment] = JsonWrites(jsv => ByteString(Json.stringify(jsv)))
  	implicit val defaultCDFormat: JsonFormat[CurriculumDevelopment] = JsonFormat(cdJsonReads, cdJsonWrites)
	
	implicit def convertJsonFormat[MODELTYPE](modelFormat: Format[MODELTYPE]): JsonFormat[MODELTYPE] =
    JsonFormat[MODELTYPE](
      JsonReads[MODELTYPE](
        bs =>
          modelFormat
            .reads(Json.parse(bs.utf8String))
            .map(result => JsonSuccess(result))
            .getOrElse[JsonResult[MODELTYPE]](JsonError())
      ),
      JsonWrites[MODELTYPE](jsv => ByteString(Json.stringify(modelFormat.writes(jsv))))
    )
}
