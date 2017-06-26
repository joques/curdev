package yester.message.processor


import akka.actor._
import play.api.libs.json.{Reads, Format, Json, Writes}
import java.util.UUID
import org.apache.kafka.clients.producer.ProducerRecord
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

import yester.util.DBManager
import yester.message.request.{ProgrammeRequestMessage, NeedAnalysisConsultationRequestMessage, NeedAnalysisSurveyRequestMessage,
    NeedAnalysisConcludeRequestMessage, NeedAnalysisBosStartRequestMessage, NeedAnalysisBosRecommendRequestMessage, NeedAnalysisSenateRecommendRequestMessage}
import yester.message.response.SimpleResponseMessage
import yester.YesterProducer
import yester.lib.{NeedAnalysis, NeedAnalysisJsonImplicits, NAConsultationComponent, NAConsultationComponentJsonImplicits, NASurveyComponent,
    NASurveyComponentJsonImplicits, NAConclusionComponent, NAConclusionComponentJsonImplicits, NABosComponent, NABosComponentJsonImplicits, NASenateComponent,
    NASenateComponentJsonImplicits }

final case class NeedAnalysisMessageProcessor(messenger: YesterProducer) extends MessageProcessor(messenger) {
    implicit val naFormat: Format[NeedAnalysis] = NeedAnalysisJsonImplicits.naFmt
    implicit val naWriter: Writes[NeedAnalysis] = NeedAnalysisJsonImplicits.naWrites

    implicit val naConsCompFormat: Format[NAConsultationComponent] = NAConsultationComponentJsonImplicits.naConsCompFmt
    implicit val naConsCompWriter: Writes[NAConsultationComponent] = NAConsultationComponentJsonImplicits.naConsCompWrites

    implicit val naSurvCompFormat: Format[NASurveyComponent] = NASurveyComponentJsonImplicits.naSurvCompFmt
    implicit val naSurvCompWriter: Writes[NASurveyComponent] = NASurveyComponentJsonImplicits.naSurvCompWrites

    implicit val naConclCompFormat: Format[NAConclusionComponent] = NAConclusionComponentJsonImplicits.naConclCompFmt
    implicit val naConclCompWriter: Writes[NAConclusionComponent] = NAConclusionComponentJsonImplicits.naConclCompWrites

    implicit val naBosCompFormat: Format[NABosComponent] = NABosComponentJsonImplicits.naBosCompFmt
    implicit val naBosCompWriter: Writes[NABosComponent] = NABosComponentJsonImplicits.naBosCompWrites

    implicit val naSenateCompFormat: Format[NASenateComponent] = NASenateComponentJsonImplicits.naSenateCompFmt
    implicit val naSenateCompWriter: Writes[NASenateComponent] = NASenateComponentJsonImplicits.naSenateCompWrites

    def receive = {
        case prgReqMsg: ProgrammeRequestMessage => {
            println("received need-analysis-start-req message ...")
            createPreProgramme(prgReqMsg)
        }
        case naConsReqMsg: NeedAnalysisConsultationRequestMessage => {
            println("received need-analysis-consult-req message ...")
            addNeedAnalysisConsultation(naConsReqMsg)
        }
        case naSurvReqMsg: NeedAnalysisSurveyRequestMessage => {
            println("received need-analysis-survey-req message ...")
            addNeedAnalysisSurvey(naSurvReqMsg)
        }
        case naConclReqMsg: NeedAnalysisConcludeRequestMessage => {
            println("received need-analysis-conclude-req message...")
            addNeedAnalysisConclusion(naConclReqMsg)
        }
        case naBSReqMsg: NeedAnalysisBosStartRequestMessage => {
            println("received need-analysis-bos-start-req message ...")
            startNABosPhase(naBSReqMsg)
        }
        case naBRReqMsg: NeedAnalysisBosRecommendRequestMessage => {
            println("received need-analysis-bos-recommend-req message ...")
            addNeedAnalysisBosRecommendation(naBRReqMsg)
        }
        case naSRReqMsg: NeedAnalysisSenateRecommendRequestMessage => {
            println("received need-analysis-senate-recommend-req message ...")
            addNeedAnalysisSenateRecommendation(naSRReqMsg)
        }
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
            case Success(needAnalysisObj) => {
                println("there is an existing need analysis object. We shall build on that...")
                needAnalysisObj.consultations match {
                    case Some(consCol) => {
                        val naConsComp = new NAConsultationComponent(consultationObj.date, consultationObj.organization, consultationObj.commitHash)
                        needAnalysisObj.consultations = Some(naConsComp :: consCol)
                    }
                    case None => {
                        val naConsComp1 = new NAConsultationComponent(consultationObj.date, consultationObj.organization, consultationObj.commitHash)
                        val consCompList1: List[NAConsultationComponent] = naConsComp1 :: Nil
                        needAnalysisObj.consultations = Some(consCompList1)
                    }
                }
                val addConsultationOpRes = DBManager.addOrUpdateNeedAnalysis(consultationObj.devCode, needAnalysisObj)
                handleInsertionResultWithSimpleResponse(addConsultationOpRes, message.messageId, "need-analysis-consult-res")
            }
            case Failure(naFailure) => {
                println("no need analysis object for this programme yet...")
                val naConsComp2 = new NAConsultationComponent(consultationObj.date, consultationObj.organization, consultationObj.commitHash)
                val consCompList2: List[NAConsultationComponent] = naConsComp2 :: Nil
                val na1: NeedAnalysis = new NeedAnalysis(Some(consCompList2), None, None, None, None)
                val addConsultationOpRes1 = DBManager.addOrUpdateNeedAnalysis(consultationObj.devCode, na1)
                handleInsertionResultWithSimpleResponse(addConsultationOpRes1, message.messageId, "need-analysis-consult-res")
            }
        }
    }

    def addNeedAnalysisSurvey(message: NeedAnalysisSurveyRequestMessage): Unit = {
        println("adding survey record for need analysis...")
        val surveyObj = message.content

        val needAnalysisObjRes = DBManager.findNeedAnalysisObject(surveyObj.devCode)
        needAnalysisObjRes.onComplete {
            case Success(needAnalysisObj) => {
                println("there is an existing need analysis object. We shall build on that...")
                val naSurvComp = new NASurveyComponent(surveyObj.commitHash)
                needAnalysisObj.survey = Some(naSurvComp)
                val addSurveyOpRes = DBManager.addOrUpdateNeedAnalysis(surveyObj.devCode, needAnalysisObj)
                handleInsertionResultWithSimpleResponse(addSurveyOpRes, message.messageId, "need-analysis-survey-res")
            }
            case Failure(naFailure) => {
                println("no need analysis object for this programme yet...")
                val naSurvComp1 = new NASurveyComponent(surveyObj.commitHash)
                val na1: NeedAnalysis = new NeedAnalysis(None, Some(naSurvComp1), None, None, None)
                val addSurveyOpRes1 = DBManager.addOrUpdateNeedAnalysis(surveyObj.devCode, na1)
                handleInsertionResultWithSimpleResponse(addSurveyOpRes1, message.messageId, "need-analysis-survey-res")
            }
        }
    }

    def addNeedAnalysisConclusion(message: NeedAnalysisConcludeRequestMessage): Unit = {
        println("adding conclusion record for need analysis...")
        val conclusionObj = message.content

        val needAnalysisObjRes = DBManager.findNeedAnalysisObject(conclusionObj.devCode)
        needAnalysisObjRes.onComplete {
            case Success(needAnalysisObj) => {
                println("there is an existing need analysis object. We shall build on that...")
                val naConclComp = new NAConclusionComponent(conclusionObj.decision, conclusionObj.commitHash)
                needAnalysisObj.conclusion = Some(naConclComp)
                val addConclusionOpRes = DBManager.addOrUpdateNeedAnalysis(conclusionObj.devCode, needAnalysisObj)
                handleInsertionResultWithSimpleResponse(addConclusionOpRes, message.messageId, "need-analysis-conclude-res")
            }
            case Failure(naFailure) => {
                println("no need analysis object for this programme yet...")
                val naConclComp1 = new NAConclusionComponent(conclusionObj.decision, conclusionObj.commitHash)
                val na1: NeedAnalysis = new NeedAnalysis(None, None, Some(naConclComp1), None, None)
                val addConclusionOpRes1 = DBManager.addOrUpdateNeedAnalysis(conclusionObj.devCode, na1)
                handleInsertionResultWithSimpleResponse(addConclusionOpRes1, message.messageId, "need-analysis-conclude-res")
            }
        }
    }

    def startNABosPhase(message: NeedAnalysisBosStartRequestMessage): Unit = {
        // this is a placeholder for the workflow manager
        println("handling bos start phase during na -- this is a placeholder for the workflow manager...")
        val naBosRespMsg: SimpleResponseMessage = new SimpleResponseMessage(message.messageId, None, Some("Ok"))
        val naBosMsgStr = Json.toJson(naBosRespMsg).toString()
        messenger.getProducer().send(new ProducerRecord[String,String]("need-analysis-bos-start-res", naBosMsgStr))
    }

    def addNeedAnalysisBosRecommendation(message: NeedAnalysisBosRecommendRequestMessage): Unit = {
        println("adding bos recommendation record for need analysis...")
        val bosRecommendationObj = message.content

        val needAnalysisObjRes = DBManager.findNeedAnalysisObject(bosRecommendationObj.devCode)
        needAnalysisObjRes.onComplete {
            case Success(needAnalysisObj) => {
                println("there is an existing need analysis object. We shall build on that...")
                val naBosComp = new NABosComponent(bosRecommendationObj.date, bosRecommendationObj.status, bosRecommendationObj.commitHash)
                needAnalysisObj.bos = Some(naBosComp)
                val addBosRecommendationOpRes = DBManager.addOrUpdateNeedAnalysis(bosRecommendationObj.devCode, needAnalysisObj)
                handleInsertionResultWithSimpleResponse(addBosRecommendationOpRes, message.messageId, "need-analysis-bos-recommend-res")
            }
            case Failure(naFailure) => {
                println("no need analysis object for this programme yet...")
                val naBosComp1 = new NABosComponent(bosRecommendationObj.date, bosRecommendationObj.status, bosRecommendationObj.commitHash)
                val na1: NeedAnalysis = new NeedAnalysis(None, None, None,  Some(naBosComp1), None)
                val addBosRecommendationOpRes1 = DBManager.addOrUpdateNeedAnalysis(bosRecommendationObj.devCode, na1)
                handleInsertionResultWithSimpleResponse(addBosRecommendationOpRes1, message.messageId, "need-analysis-bos-recommend-res")
            }
        }
    }

    def addNeedAnalysisSenateRecommendation(message: NeedAnalysisSenateRecommendRequestMessage): Unit = {
        println("adding senate recommendation record for need analysis...")
        val senateRecommendationObj = message.content

        val needAnalysisObjRes = DBManager.findNeedAnalysisObject(senateRecommendationObj.devCode)
        needAnalysisObjRes.onComplete {
            case Success(needAnalysisObj) => {
                println("there is an existing need analysis object. We shall build on that...")
                val naSenateComp = new NABosComponent(senateRecommendationObj.date, senateRecommendationObj.status, senateRecommendationObj.commitHash)
                needAnalysisObj.senate = Some(naSenateComp)
                val addSenateRecommendationOpRes = DBManager.addOrUpdateNeedAnalysis(senateRecommendationObj.devCode, needAnalysisObj)
                handleInsertionResultWithSimpleResponse(addSenateRecommendationOpRes, message.messageId, "need-analysis-senate-recommend-res")
            }
            case Failure(naFailure) => {
                println("no need analysis object for this programme yet ...")
                val naSenComp1 = new NASenateComponent(senateRecommendationObj.date, senateRecommendationObj.status, senateRecommendationObj.commitHash)
                val na1: NeedAnalysis = new NeedAnalysis(None, None, None,  None, Some(naSenComp1))
                val addSenateRecommendationOpRes1 = DBManager.addOrUpdateNeedAnalysis(senateRecommendationObj.devCode, na1)
                handleInsertionResultWithSimpleResponse(addSenateRecommendationOpRes1, message.messageId, "need-analysis-senate-recommend-res")
            }
        }
    }
}
