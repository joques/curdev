package yester.lib

import com.couchbase.client.scala.implicits.Codec
import play.api.libs.json.{Json, Format, Writes}

import NAConsultationComponent._
import NASurveyComponent._
import NAConclusionComponent._
import NABosComponent._
import NASenateComponent._
import NAAPCComponent._

final case class NeedAnalysis(consultations: Option[List[NAConsultationComponent]], survey: Option[NASurveyComponent], conclusion: Option[NAConclusionComponent], bos: Option[NABosComponent], senate: Option[NASenateComponent], apc: Option[NAAPCComponent])

object NeedAnalysis {
	implicit val nacCodec: Codec[NeedAnalysis] = Codec.codec[NeedAnalysis]

	implicit val nacosulFormat: Format[NAConsultationComponent] = naConsCompFmt
	implicit val nasurvFormat: Format[NASurveyComponent] = naSurvCompFmt
	implicit val naconFormat: Format[NAConclusionComponent] = naConclCompFmt
	implicit val nabosFormat: Format[NABosComponent] = naBosCompFmt
	implicit val nasenFormat: Format[NASenateComponent] = naSenateCompFmt
    implicit val naapcFormat: Format[NAAPCComponent] = naAPCCompFmt

	implicit val naFmt = Json.format[NeedAnalysis]
    implicit val naWrites = Json.writes[NeedAnalysis]
    implicit val naReads = Json.reads[NeedAnalysis]
}
