package yester.message.processor

import akka.actor._
import play.api.libs.json.{Reads, Format, Json, Writes}
import java.util.UUID

import yester.util.DBManager
import yester.message.request.{ProgrammeRequestMessage, NeedAnalysisConsultationRequestMessage}
import yester.YesterProducer
import yester.lib.{NeedAnalysis, NeedAnalysisJsonImplicits, NAConsultationComponent, NAConsultationComponentJsonImplicits}

final case class NeedAnalysisMessageProcessor(messenger: YesterProducer) extends MessageProcessor(messenger) {
    implicit val naConsCompFormat: Format[NAConsultationComponent] = NAConsultationComponentJsonImplicits.naFmt
    implicit val naConsCompWriter: Writes[NAConsultationComponent] = NAConsultationComponentJsonImplicits.naWrites

    implicit val naFormat: Format[NeedAnalysis] = NeedAnalysisJsonImplicits.naFmt
    implicit val naWriter: Writes[NeedAnalysis] = NeedAnalysisJsonImplicits.naWrites

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

        val needAnalysisObjRes = DBManager.findNeedAnalysisObject(consultationObj.devCode)
        needAnalysisObjRes.onComplete {
            case Success(needAnalysisObj) => {}
            case Failure(naFailure) => {
                println("no need analysis object for this programme yet...")
                val naConsComp = new NAConsultationComponent(consultationObj.date, consultationObj.organization, consultationObj.commitHash)
                val consCompList: List[NAConsultationComponent] = List(naConsComp)
                val na1: NeedAnalysis = new NeedAnalysis(Some(consCompList, None, None))
                val addConsultationOpRes = DB.addOrUpdateNeedAnalysis(consultationObj.devCode, na1)
                handleInsertionResultWithSimpleResponse(addConsultationOpRes, message.messageId, "need-analysis-consult-res")
            }
        }
    }

    def addNeedAnalysisSurvey(message: NeedAnalysisSurveyRequestMessage): Unit = {
        println("adding survey record for need analysis...")
        val surveyObj = message.content
        val surveyKey = UUID.randomUUID().toString()
        val addSurveyOpRes = DBManager.addNeedAnalysisConsultation(surveyObj.devCode, surveyObj)
        handleInsertionResultWithSimpleResponse(addSurveyOpRes, message.messageId, "need-analysis-survey-res")
    }

    def addNeedAnalysisConclusion(message: NeedAnalysisConcludeRequestMessage): Unit = {
        println("adding conclusion record for need analysis...")
        val conclusionObj = message.content
        val addConclusionOpRes = DBManager.addNeedAnalysisConclusion(conclusionObj.devCode, conclusionObj)
        handleInsertionResultWithSimpleResponse(addConclusionOpRes, message.messageId, "need-analysis-conclude-res")
    }
}
