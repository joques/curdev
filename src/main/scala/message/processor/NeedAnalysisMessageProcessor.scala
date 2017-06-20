package yester.message.processor

import akka.actor._
import play.api.libs.json.{Reads, Json, Writes}
import java.util.UUID

import yester.util.DBManager
import yester.message.request.{ProgrammeRequestMessage, NeedAnalysisConsultationRequestMessage}
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
            println("received need-analysis-survey-req message ...")
            addNeedAnalysisSurvey(naSurvReqMsg)
        case naConclReqMsg: NeedAnalysisConcludeRequestMessage =>
            println("received need-analysis-conclude-req message...")
            addNeedAnalysisConclusion(naConclReqMsg)
        case _ =>
            println("unknown message ...")
    }

    def createPreProgramme(message: ProgrammeRequestMessage): Unit = {
        println("creating a new programme object...")

        val progObj = message.content
        val progKey = UUID.randomUUID().toString()
        val createProgOpRes = DBManager.createProgramme(progKey, progObj)

        handleInsertionResultWithSimpleResponse(createProgOpRes, message.messageId, "need-analysis-start-res")
    }

    def addNeedAnalysisConsultation(message: NeedAnalysisConsultationRequestMessage): Unit = {
        println("adding consultation record for need analysis...")

        val consultationObj = message.content
        val consulationKey = UUID.randomUUID().toString()

        val addConsultationOpRes = DBManager.addNeedAnalysisConsultation(consulationKey, consultationObj)

        handleInsertionResultWithSimpleResponse(addConsultationOpRes, message.messageId, "need-analysis-consult-res")
    }

    def addNeedAnalysisSurvey(message: NeedAnalysisSurveyRequestMessage): Unit = {
        println("adding survey record for need analysis...")

        val surveyObj = message.content
        val surveyKey = UUID.randomUUID().toString()

        val addSurveyOpRes = DBManager.addNeedAnalysisConsultation(surveyKey, surveyObj)

        handleInsertionResultWithSimpleResponse(addSurveyOpRes, message.messageId, "need-analysis-survey-res")
    }

    def addNeedAnalysisConclusion(message: NeedAnalysisConcludeRequestMessage): Unit = {
        println("adding conclusion record for need analysis...")

        val conclusionObj = message.content
        val conclusionKey = UUID.randomUUID().toString()

        val addConclusionOpRes = DBManager.addNeedAnalysisConclusion(conclusionKey, conclusionObj)

        handleInsertionResultWithSimpleResponse(addConclusionOpRes, message.messageId, "need-analysis-conclude-res")
    }
}
