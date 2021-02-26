package yester.lib

import com.couchbase.client.scala.implicits.Codec

// import akka.util.ByteString
// import play.api.libs.json.{Json, Format, Writes}
// import org.reactivecouchbase.rs.scaladsl.json.{JsonReads, JsonWrites, JsonFormat, JsonSuccess, JsonResult, JsonError}

import NAConsultationComponent._
import NASurveyComponent._
import NAConclusionComponent._
import NABosComponent._
import NASenateComponent._

final case class NeedAnalysis(consultations: Option[List[NAConsultationComponent]], survey: Option[NASurveyComponent], conclusion: Option[NAConclusionComponent], bos: Option[NABosComponent], senate: Option[NASenateComponent])

object NeedAnalysis {
  implicit val nacCodec: Codec[NeedAnalysis] = Codec.codec[NeedAnalysis]
}

// object NeedAnalysisJsonImplicits {
//     implicit val naConclCompFmt: Format[NAConclusionComponent] = NAConclusionComponentJsonImplicits.naConclCompFmt
//     implicit val naConsCompFmt: Format[NAConsultationComponent] = NAConsultationComponentJsonImplicits.naConsCompFmt
//     implicit val naSurvCompFmt: Format[NASurveyComponent] = NASurveyComponentJsonImplicits.naSurvCompFmt
//     implicit val naBosCompFmt: Format[NABosComponent] = NABosComponentJsonImplicits.naBosCompFmt
//     implicit val naSenateCompFmt: Format[NASenateComponent] = NASenateComponentJsonImplicits.naSenateCompFmt

//     implicit val naFmt = Json.format[NeedAnalysis]
//     implicit val naWrites = Json.writes[NeedAnalysis]
//     implicit val naReads = Json.reads[NeedAnalysis]
		
// 	def convertJsonFormat[NeedAnalysis](modelFormat: Format[NeedAnalysis]): JsonFormat[NeedAnalysis] =
//     JsonFormat[NeedAnalysis](
//       JsonReads[NeedAnalysis](
//         bs =>
//           modelFormat
//             .reads(Json.parse(bs.utf8String))
//             .map(result => JsonSuccess(result))
//             .getOrElse[JsonResult[NeedAnalysis]](JsonError())
//       ),
//       JsonWrites[NeedAnalysis](jsv => ByteString(Json.stringify(modelFormat.writes(jsv))))
//     )
	
// 	implicit val naJsonFormat: JsonFormat[NeedAnalysis] = convertJsonFormat(naFmt)
// }
