package yester.message.processor

import akka.actor._
import org.apache.kafka.clients.producer.ProducerRecord
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{Reads, Json, Writes}
import java.util.UUID
import yester.YesterProducer
import yester.util.DBManager
import yester.lib.{PreProgrammeComponent, Programme}
import yester.message.request.{CurriculumReviewRequestMessage, CurriculumDevelopmentAppointPACRequestMessage, CurriculumDevelopmentDraftRevisionRequestMessage,
    CurriculumDevelopmentDraftRevisionRequestMessageJsonImplicits
}
import yester.message.response.SimpleResponseMessage

final case class CurriculumDevelopmentMessageProcessor(messenger: YesterProducer) extends MessageProcessor(messenger) {
    def receive = {
        case curDevReqMsg: CurriculumReviewRequestMessage => {
            println("received curriculum-review message ...")
            startCurriculumReview(curDevReqMsg)
        }
        case pacMembersReqMsg: CurriculumDevelopmentAppointPACRequestMessage => {
            println("received pac member appointment request ...")
            addPACMembers(pacMembersReqMsg)
        }
        case draftRevisionReqMsg: CurriculumDevelopmentDraftRevisionRequestMessage => {
            println("received draft revision request for curriculum development ...")
            addDraftRevision(draftRevisionReqMsg)
        }
        case _ =>
            println("unknown message type ...")
    }

    def startCurriculumReview(message: CurriculumReviewRequestMessage): Unit = {
        println("starting a programme review ...")

        val reviewObj = message.content

        val allProgs = DBManager.findAllProgrammes()
        allProgs.onComplete {
            case Failure(allProgsError) => {
                val progErrorRespMsg: SimpleResponseMessage = new SimpleResponseMessage(message.messageId, Option(allProgsError.getMessage), None)
                val errMsgStr = Json.toJson(progErrorRespMsg).toString
                println(s"the error message to be sent for all progs is $errMsgStr")
                messenger.getProducer().send(new ProducerRecord[String,String]("curriculum-review-res", errMsgStr))
            }
            case Success(progList) => {
                val toBeReviewedProgs: List[Programme] = progList.filter((prg: Programme) => ((! prg.isPreProgramme) && (prg.progComponent.get.code == reviewObj.code)))
                if (toBeReviewedProgs.isEmpty) {
                    val progErrorRespMsg1: SimpleResponseMessage = new SimpleResponseMessage(message.messageId, Option(s"No exisiting programme with code $reviewObj.code"), None)
                    val errMsgStr1 = Json.toJson(progErrorRespMsg1).toString
                    println(s"the error message to be sent for all progs is $errMsgStr1")
                    messenger.getProducer().send(new ProducerRecord[String,String]("curriculum-review-res", errMsgStr1))
                }
                else {
                    val beingReviewed: Programme = toBeReviewedProgs.head
                    val currentPreProgCom: Option[PreProgrammeComponent] = Some(new PreProgrammeComponent(reviewObj.devCode, reviewObj.initiator))
                    val newReviewProg: Programme = new Programme(beingReviewed.faculty, beingReviewed.department, beingReviewed.name, beingReviewed.level, false, None, currentPreProgCom)

                    val reviewKey = UUID.randomUUID().toString()
                    val createProgOpRes = DBManager.createProgramme(reviewKey, newReviewProg)

                    handleInsertionResultWithSimpleResponse(createProgOpRes, message.messageId, "curriculum-review-res")
                }
            }
        }
    }

    def addPACMembers(message: CurriculumDevelopmentAppointPACRequestMessage): Unit = {
        println("adding PAC members ...")
        val memberObj = message.content

        val curDevObjRes = DBManager.findCurriculumDevelopmentObject(memberObj.devCode)
        curDevObjRes.onComplete {
            case Success(curDevObj) => {
                println("there could be an existing curriculum development object. We shall explore further ...")

                curDevObj match {
                    case Some(cdo) => {
                        println("there is an existing object ...")
                        val curDev3: CurriculumDevelopment = new CurriculumDevelopment(Some(memberObj.members), cdo.submissionDate, cdo.validated)
                        saveCurriculumDevelopmentObject(message.messageId, memberObj.devCode, curDev3, "cur-dev-appoint-pac-res")
                    }
                    case None => {
                        println("it seems like there was no object at all...")
                        val curDev2: CurriculumDevelopment = new CurriculumDevelopment(Some(memberObj.members), None, false)
                        saveCurriculumDevelopmentObject(message.messageId, memberObj.devCode, curDev2, "cur-dev-appoint-pac-res")
                    }
                }
            }
            case Failure(curDevFailure) => {
                println("no curriculum development object exists yet...")
                val curDev1: CurriculumDevelopment = new CurriculumDevelopment(Some(memberObj.members), None, false)
                saveCurriculumDevelopmentObject(message.messageId, memberObj.devCode, curDev1, "cur-dev-appoint-pac-res")
            }
        }
    }

    def addDraftRevision(message: CurriculumDevelopmentDraftRevisionRequestMessage): Unit => {
        println("adding draft revision...")
        val simpleSuccessRespMsg: SimpleResponseMessage = new SimpleResponseMessage(messamge.messageId, None, Option("ok"))
        val succMsgStr = Json.toJson(simpleSuccessRespMsg).toString()
        messenger.getProducer().send(new ProducerRecord[String,String]("cur-dev-draft-revise-res", succMsgStr))
    }

    def saveCurriculumDevelopmentObject(messageId: String, devCode: String,  curDev: CurriculumDevelopment, respTopic: String): Unit = {
        val addCurDevOpRes = DBManager.upsertCurriculumDevelopment(devCode, curDev)
        handleInsertionResultWithSimpleResponse(addCurDevOpRes, messageId, respTopic)
    }
}
