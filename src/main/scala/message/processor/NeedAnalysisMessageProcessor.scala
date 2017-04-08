package yester.message.processor

import akka.actor._
import play.api.libs.json.{Reads, Json, Writes}
import java.util.UUID
import scala.concurrent.Future

import yester.util.DBManager
import yester.message.request.{ProgrammeRequestMessage, NeedAnalysisConsultationRequestMessage, NeedAnalysisSurveyRequestMessage}
import yester.lib.{Programme, ProgrammeJsonImplicits, NeedAnalysisConsultation, NeedAnalysisConsultationJsonImplicits, NeedAnalysisSurvey, NeedAnalysisSurveyJsonImplicits}
import yester.YesterProducer

final case class NeedAnalysisMessageProcessor(messenger: YesterProducer) extends MessageProcessor(messenger) {
    def receive = {
        case prgReqMsg: ProgrammeRequestMessage =>
            println("received need-analysis-start-req message ...")
            createPreProgramme(prgReqMsg)
        case naConsReqMsg: NeedAnalysisConsultationRequestMessage =>
            println("received need-analysis-consult-req message ...")
            addNeedAnalysisConsultation(naConsReqMsg)
        case naSurvReqMsg: NeedAnalysisSurveyRequestMessage =>
            println("received need-analysis-conclude-req message ...")
            addNeedAnalysisSurveyData(naSurvReqMsg)
        case _ =>
            println("unknown message ...")
    }

    def createPreProgramme(message: ProgrammeRequestMessage): Unit = {
        println("creating a new programme object ...")

        val progObj = message.content
        val progKey = UUID.randomUUID().toString()
        val createProgRes: Future[Programme] = DBManager.createProgramme(progKey, progObj)

        handleInsertionResultWithSimpleResponse[Programme](createProgRes, message.messageId, "need-analysis-start-res")
    }

    def addNeedAnalysisConsultation(message: NeedAnalysisConsultationRequestMessage): Unit = {
        println("adding consultation record for need analysis")

        val consultationObj = message.content
        val consulationKey = UUID.randomUUID().toString()

        val addConsultationRes: Future[NeedAnalysisConsultation] = DBManager.addNeedAnalysisConsultation(consulationKey, consultationObj)

        handleInsertionResultWithSimpleResponse[NeedAnalysisConsultation](addConsultationRes, message.messageId, "need-analysis-consult-res")
    }

    def addNeedAnalysisSurveyData(message: NeedAnalysisSurveyRequestMessage): Unit = {
        println("adding survey data for need analysis")

        var surveyObj = message.content
        val surveyKey = UUID.randomUUID().toString()

        val addSurveyRes: Future[NeedAnalysisSurvey] = DBManager.addNeedAnalysisSurvey(surveyKey, surveyObj)
        handleInsertionResultWithSimpleResponse[NeedAnalysisSurvey](addSurveyRes, message.messageId, "need-analysis-conclude-res")
    }
}
