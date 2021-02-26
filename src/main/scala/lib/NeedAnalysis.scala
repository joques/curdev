package yester.lib

import com.couchbase.client.scala.implicits.Codec
import play.api.libs.json.{Json, Format, Writes}

import NAConsultationComponent._
import NASurveyComponent._
import NAConclusionComponent._
import NABosComponent._
import NASenateComponent._

final case class NeedAnalysis(consultations: Option[List[NAConsultationComponent]], survey: Option[NASurveyComponent], conclusion: Option[NAConclusionComponent], bos: Option[NABosComponent], senate: Option[NASenateComponent])

object NeedAnalysis {
	implicit val nacCodec: Codec[NeedAnalysis] = Codec.codec[NeedAnalysis]

	implicit val naFmt = Json.format[NeedAnalysis]
    implicit val naWrites = Json.writes[NeedAnalysis]
    implicit val naReads = Json.reads[NeedAnalysis]
}