package yester.message.processor


import akka.actor._
import play.api.libs.json.{Reads, Format, Json, Writes}
import java.util.UUID
import org.apache.kafka.clients.producer.ProducerRecord
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

import yester.util.DBManager
import yester.message.request.{ProgrammeRequestMessage, NeedAnalysisConsultationRequestMessage, NeedAnalysisSurveyRequestMessage,
    NeedAnalysisConcludeRequestMessage, NeedAnalysisBosStartRequestMessage, NeedAnalysisBosRecommendRequestMessage, NeedAnalysisSenateRecommendRequestMessage,
    NeedAnalysisSenateStartRequestMessage}
import yester.message.response.SimpleResponseMessage
import yester.YesterProducer
import yester.lib.{NeedAnalysis, NeedAnalysisJsonImplicits, NAConsultationComponent, NAConsultationComponentJsonImplicits, NASurveyComponent,
    NASurveyComponentJsonImplicits, NAConclusionComponent, NAConclusionComponentJsonImplicits, NABosComponent, NABosComponentJsonImplicits, NASenateComponent,
    NASenateComponentJsonImplicits, Programme, ProgrammeJsonImplicits }

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
        case naSSReqMsg: NeedAnalysisSenateStartRequestMessage => {
            println("received need-analysis-senate-start-req message ...")
            startNASenatePhase(naSSReqMsg)
        }
        case _ =>
            println("unknown message ...")
    }

    def createPreProgramme(message: ProgrammeRequestMessage): Unit = {
        println("creating a new programme object...")

        val progObj = message.content
        val progKey = UUID.randomUUID().toString()
        val createProgRes = DBManager.createProgramme(progKey, progObj)

        handleInsertionResultWithSimpleResponse[Programme](createProgRes, message.messageId, "need-analysis-start-res")
    }

    def addNeedAnalysisConsultation(message: NeedAnalysisConsultationRequestMessage): Unit = {
        println("adding consultation record for need analysis...")
        val consultationObj = message.content

        val needAnalysisObjRes = DBManager.findNeedAnalysisObject(consultationObj.devCode)
        needAnalysisObjRes.onComplete {
            case Success(needAnalysisObj) => {
                println("there is an existing need analysis object. We shall build on that...")
                needAnalysisObj match {
                    case Some(naObj) => {
                        needAnalysisObj.get.consultations match {
                            case Some(consCol) => {
                                val naConsComp = new NAConsultationComponent(consultationObj.date, consultationObj.organization, consultationObj.commitHash)
                                val na2 = new NeedAnalysis(Some(naConsComp :: consCol), naObj.survey, naObj.conclusion, naObj.bos, naObj.senate)
                                val addConsultationRes2 = DBManager.addOrUpdateNeedAnalysis(consultationObj.devCode, na2)
                                handleInsertionResultWithSimpleResponse[NeedAnalysis](addConsultationRes2, message.messageId, "need-analysis-consult-res")
                            }
                            case None => {
                                val naConsComp1 = new NAConsultationComponent(consultationObj.date, consultationObj.organization, consultationObj.commitHash)
                                val consCompList1: List[NAConsultationComponent] = naConsComp1 :: Nil
                                val na3 = new NeedAnalysis(Some(consCompList1), naObj.survey, naObj.conclusion, naObj.bos, naObj.senate)
                                val addConsultationRes3 = DBManager.addOrUpdateNeedAnalysis(consultationObj.devCode, na3)
                                handleInsertionResultWithSimpleResponse[NeedAnalysis](addConsultationRes3, message.messageId, "need-analysis-consult-res")
                            }
                        }
                    }
                    case None => {
                        println("there was actually no need analysis object...")
                        val naConsComp4 = new NAConsultationComponent(consultationObj.date, consultationObj.organization, consultationObj.commitHash)
                        val consCompList4: List[NAConsultationComponent] = naConsComp4 :: Nil
                        val na4: NeedAnalysis = new NeedAnalysis(Some(consCompList4), None, None, None, None)
                        val addConsultationRes4 = DBManager.addOrUpdateNeedAnalysis(consultationObj.devCode, na4)
                        handleInsertionResultWithSimpleResponse[NeedAnalysis](addConsultationRes4, message.messageId, "need-analysis-consult-res")
                    }
                }
            }
            case Failure(naFailure) => {
                println("no need analysis object for this programme yet...")
                val naConsComp2 = new NAConsultationComponent(consultationObj.date, consultationObj.organization, consultationObj.commitHash)
                val consCompList2: List[NAConsultationComponent] = naConsComp2 :: Nil
                val na1: NeedAnalysis = new NeedAnalysis(Some(consCompList2), None, None, None, None)
                val addConsultationRes1 = DBManager.addOrUpdateNeedAnalysis(consultationObj.devCode, na1)
                handleInsertionResultWithSimpleResponse[NeedAnalysis](addConsultationRes1, message.messageId, "need-analysis-consult-res")
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
                needAnalysisObj match {
                    case Some(naObj) => {
                        val naSurvComp = new NASurveyComponent(surveyObj.commitHash)
                        val na: NeedAnalysis = new NeedAnalysis(naObj.consultations, Some(naSurvComp), naObj.conclusion, naObj.bos, naObj.senate)
                        val addSurveyRes = DBManager.addOrUpdateNeedAnalysis(surveyObj.devCode, na)
                        handleInsertionResultWithSimpleResponse[NeedAnalysis](addSurveyRes, message.messageId, "need-analysis-survey-res")
                    }
                    case None => {
                        println("there was actually no need analysis object...")
                        val naSurvComp2 = new NASurveyComponent(surveyObj.commitHash)
                        val na2: NeedAnalysis = new NeedAnalysis(None, Some(naSurvComp2), None, None, None)
                        val addSurveyRes2 = DBManager.addOrUpdateNeedAnalysis(surveyObj.devCode, na2)
                        handleInsertionResultWithSimpleResponse[NeedAnalysis](addSurveyRes2, message.messageId, "need-analysis-survey-res")
                    }
                }
            }
            case Failure(naFailure) => {
                println("no need analysis object for this programme yet...")
                val naSurvComp1 = new NASurveyComponent(surveyObj.commitHash)
                val na1: NeedAnalysis = new NeedAnalysis(None, Some(naSurvComp1), None, None, None)
                val addSurveyRes1 = DBManager.addOrUpdateNeedAnalysis(surveyObj.devCode, na1)
                handleInsertionResultWithSimpleResponse[NeedAnalysis](addSurveyRes1, message.messageId, "need-analysis-survey-res")
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
                needAnalysisObj match {
                    case Some(naObj) => {
                        val naConclComp = new NAConclusionComponent(conclusionObj.decision, conclusionObj.commitHash)
                        val na: NeedAnalysis = new NeedAnalysis(naObj.consultations, naObj.survey, Some(naConclComp), naObj.bos, naObj.senate)
                        val addConclusionRes = DBManager.addOrUpdateNeedAnalysis(conclusionObj.devCode, na)
                        handleInsertionResultWithSimpleResponse[NeedAnalysis](addConclusionRes, message.messageId, "need-analysis-conclude-res")
                    }
                    case None => {
                        println("there was actually no need analysis object...")
                        val naConclComp2 = new NAConclusionComponent(conclusionObj.decision, conclusionObj.commitHash)
                        val na2: NeedAnalysis = new NeedAnalysis(None, None, Some(naConclComp2), None, None)
                        val addConclusionRes2 = DBManager.addOrUpdateNeedAnalysis(conclusionObj.devCode, na2)
                        handleInsertionResultWithSimpleResponse[NeedAnalysis](addConclusionRes2, message.messageId, "need-analysis-conclude-res")
                    }
                }
            }
            case Failure(naFailure) => {
                println("no need analysis object for this programme yet...")
                val naConclComp1 = new NAConclusionComponent(conclusionObj.decision, conclusionObj.commitHash)
                val na1: NeedAnalysis = new NeedAnalysis(None, None, Some(naConclComp1), None, None)
                val addConclusionRes1 = DBManager.addOrUpdateNeedAnalysis(conclusionObj.devCode, na1)
                handleInsertionResultWithSimpleResponse[NeedAnalysis](addConclusionRes1, message.messageId, "need-analysis-conclude-res")
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

    def startNASenatePhase(message: NeedAnalysisSenateStartRequestMessage): Unit = {
        // this is a placeholder for the workflow manager
        println("handling senate start phase during na -- this is a placeholder for the workflow manager...")
        val naSenateRespMsg: SimpleResponseMessage = new SimpleResponseMessage(message.messageId, None, Some("Ok"))
        val naSenateMsgStr = Json.toJson(naSenateRespMsg).toString()
        messenger.getProducer().send(new ProducerRecord[String,String]("need-analysis-senate-start-res", naSenateMsgStr))
    }

    def addNeedAnalysisBosRecommendation(message: NeedAnalysisBosRecommendRequestMessage): Unit = {
        println("adding bos recommendation record for need analysis...")
        val bosRecommendationObj = message.content

        val needAnalysisObjRes = DBManager.findNeedAnalysisObject(bosRecommendationObj.devCode)
        needAnalysisObjRes.onComplete {
            case Success(needAnalysisObj) => {
                println("there is an existing need analysis object. We shall build on that...")
                needAnalysisObj match {
                    case Some(naObj) => {
                        val naBosComp = new NABosComponent(bosRecommendationObj.date, bosRecommendationObj.status, bosRecommendationObj.commitHash)
                        val na: NeedAnalysis = new NeedAnalysis(naObj.consultations, naObj.survey, naObj.conclusion,  Some(naBosComp), naObj.senate)
                        val addBosRecommendationRes = DBManager.addOrUpdateNeedAnalysis(bosRecommendationObj.devCode, na)
                        handleInsertionResultWithSimpleResponse[NeedAnalysis](addBosRecommendationRes, message.messageId, "need-analysis-bos-recommend-res")
                    }
                    case None => {
                        println("there was actually no need analysis...")
                        val naBosComp2 = new NABosComponent(bosRecommendationObj.date, bosRecommendationObj.status, bosRecommendationObj.commitHash)
                        val na2: NeedAnalysis = new NeedAnalysis(None, None, None,  Some(naBosComp2), None)
                        val addBosRecommendationRes2 = DBManager.addOrUpdateNeedAnalysis(bosRecommendationObj.devCode, na2)
                        handleInsertionResultWithSimpleResponse[NeedAnalysis](addBosRecommendationRes2, message.messageId, "need-analysis-bos-recommend-res")
                    }
                }
            }
            case Failure(naFailure) => {
                println("no need analysis object for this programme yet...")
                val naBosComp1 = new NABosComponent(bosRecommendationObj.date, bosRecommendationObj.status, bosRecommendationObj.commitHash)
                val na1: NeedAnalysis = new NeedAnalysis(None, None, None,  Some(naBosComp1), None)
                val addBosRecommendationRes1 = DBManager.addOrUpdateNeedAnalysis(bosRecommendationObj.devCode, na1)
                handleInsertionResultWithSimpleResponse[NeedAnalysis](addBosRecommendationRes1, message.messageId, "need-analysis-bos-recommend-res")
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
                needAnalysisObj match {
                    case Some(naObj) => {
                        val naSenateComp = new NASenateComponent(senateRecommendationObj.date, senateRecommendationObj.status, senateRecommendationObj.commitHash)
                        val na: NeedAnalysis = new NeedAnalysis(naObj.consultations, naObj.survey, naObj.conclusion,  naObj.bos, Some(naSenateComp))
                        val addSenateRecommendationRes = DBManager.addOrUpdateNeedAnalysis(senateRecommendationObj.devCode, na)
                        handleInsertionResultWithSimpleResponse[NeedAnalysis](addSenateRecommendationRes, message.messageId, "need-analysis-senate-recommend-res")
                    }
                    case None => {
                        println("there was actually no need analysis object ...")
                        val naSenComp2 = new NASenateComponent(senateRecommendationObj.date, senateRecommendationObj.status, senateRecommendationObj.commitHash)
                        val na2: NeedAnalysis = new NeedAnalysis(None, None, None,  None, Some(naSenComp2))
                        val addSenateRecommendationRes2 = DBManager.addOrUpdateNeedAnalysis(senateRecommendationObj.devCode, na2)
                        handleInsertionResultWithSimpleResponse[NeedAnalysis](addSenateRecommendationRes2, message.messageId, "need-analysis-senate-recommend-res")
                    }
                }
            }
            case Failure(naFailure) => {
                println("no need analysis object for this programme yet ...")
                val naSenComp1 = new NASenateComponent(senateRecommendationObj.date, senateRecommendationObj.status, senateRecommendationObj.commitHash)
                val na1: NeedAnalysis = new NeedAnalysis(None, None, None,  None, Some(naSenComp1))
                val addSenateRecommendationRes1 = DBManager.addOrUpdateNeedAnalysis(senateRecommendationObj.devCode, na1)
                handleInsertionResultWithSimpleResponse[NeedAnalysis](addSenateRecommendationRes1, message.messageId, "need-analysis-senate-recommend-res")
            }
        }
    }
}
