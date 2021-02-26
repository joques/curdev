package yester.message.processor


import akka.actor._
import org.apache.kafka.clients.producer.ProducerRecord
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{Reads, Json, Writes}
import java.util.UUID
import yester.YesterProducer
import yester.util.DBManager
import yester.lib.{PreProgrammeComponent, Programme, CurriculumDevelopment}
import yester.message.request.{CurriculumReviewRequestMessage, CurriculumDevelopmentAppointPACRequestMessage, CurriculumDevelopmentDraftRevisionRequestMessage,
    CurriculumDevelopmentDraftRevisionRequestMessageJsonImplicits, CurriculumDevelopmentDraftSubmissionRequestMessage,
    CurriculumDevelopmentDraftSubmissionRequestMessageJsonImplicits, CurriculumDevelopmentDraftValidationRequestMessage,
    CurriculumDevelopmentDraftValidationRequestMessageJsonImplicits, CurriculumDevelopmentAppointCDCRequestMessage,
    CurriculumDevelopmentAppointCDCRequestMessageJsonImplicits
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
        case cdcMembersReqMsg: CurriculumDevelopmentAppointCDCRequestMessage => {
            println("received cdc member appoitment request ...")
            addCDCMembers(cdcMembersReqMsg)
        }
        case draftRevisionReqMsg: CurriculumDevelopmentDraftRevisionRequestMessage => {
            println("received draft revision request for curriculum development ...")
            handleDraftRevision(draftRevisionReqMsg)
        }
        case draftSubmissionReqMsg: CurriculumDevelopmentDraftSubmissionRequestMessage => {
            println("received draft submission request for curriculum development ...")
            handleDraftSubmission(draftSubmissionReqMsg)
        }
        case draftValidationReqMsg: CurriculumDevelopmentDraftValidationRequestMessage => {
            println("received draft validation request for curriculum development ...")
            handleDraftValidation(draftValidationReqMsg)
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
            case Success(progSeq) => {
                progs.rowsAs[Programme] match {
                    case Failure(rowError) => {
                        val rowErrorRespMsg: UserWithPreProgrammeResponseMessage = new UserWithPreProgrammeResponseMessage(message.simpleMsg.messageId, Option(rowError.getMessage), None)
                        val errMsgStr11 = Json.toJson(rowErrorRespMsg).toString()
                        println(s"the error message to be sent is $errMsgStr11")
                        messenger.getProducer().send(new ProducerRecord[String,String]("curriculum-review-res", errMsgStr11))
                    }
                    case Success(progSeq) => {
                        val toBeReviewedProgs: Seq[Programme] = progSeq.filter((prg: Programme) => ((! prg.isPreProgramme) && (prg.progComponent.get.code == reviewObj.code)))
                        if (toBeReviewedProgs.isEmpty) {
                            val progErrorRespMsg1: SimpleResponseMessage = new SimpleResponseMessage(message.messageId, Option(s"No exisiting programme with code $reviewObj.code"), None)
                            val errMsgStr1 = Json.toJson(progErrorRespMsg1).toString
                            println(s"the error message to be sent for all progs is $errMsgStr1")
                            messenger.getProducer().send(new ProducerRecord[String,String]("curriculum-review-res", errMsgStr1))
                        } else {
                            val beingReviewed: Programme = toBeReviewedProgs.head
                            val currentPreProgCom: Option[PreProgrammeComponent] = Some(new PreProgrammeComponent(reviewObj.devCode, reviewObj.initiator))
                            val newReviewProg: Programme = new Programme(beingReviewed.faculty, beingReviewed.department, beingReviewed.name, beingReviewed.level, false, None, currentPreProgCom)

                            val reviewKey = UUID.randomUUID().toString()
                            val createProgRes = DBManager.createProgramme(reviewKey, newReviewProg)

                            handleInsertionResultWithSimpleResponse(createProgRes, message.messageId, "curriculum-review-res")
                        }
                    }
                }
            }
        }
    }

    def addPACMembers(message: CurriculumDevelopmentAppointPACRequestMessage): Unit = {
        println("adding PAC members ...")
        val memberObj = message.content

        val curDevObjRes = DBManager.findCurriculumDevelopmentObject(memberObj.devCode)
        curDevObjRes.onComplete {
            case Failure(curDevFailure) => {
                println("no curriculum development object exists yet...")
                val curDev1: CurriculumDevelopment = new CurriculumDevelopment(Some(memberObj.members), None, None, None)
                saveCurriculumDevelopmentObject(message.messageId, memberObj.devCode, curDev1, "cur-dev-appoint-pac-res")
            }
            case Success(cdo) => {
                println("there is an existing object ...")
                val curDev3: CurriculumDevelopment = new CurriculumDevelopment(Some(memberObj.members), cdo.cdcMembers, cdo.submissionDate, cdo.decision)
                saveCurriculumDevelopmentObject(message.messageId, memberObj.devCode, curDev3, "cur-dev-appoint-pac-res")
            }
        }
    }

    def addCDCMembers(message: CurriculumDevelopmentAppointCDCRequestMessage): Unit = {
        println("adding CDC members ...")
        val memberObj = message.content

        val curDevObjRes = DBManager.findCurriculumDevelopmentObject(memberObj.devCode)
        curDevObjRes.onComplete {
            case Failure(curDevFailure) => {
                println("no curriculum development object exists yet...")
                val curDev1: CurriculumDevelopment = new CurriculumDevelopment(None, Some(memberObj.members), None, None)
                saveCurriculumDevelopmentObject(message.messageId, memberObj.devCode, curDev1, "cur-dev-appoint-cdc-res")
            }
            case Success(cdo) => {
                println("there is an existing object ...")
                val curDev3: CurriculumDevelopment = new CurriculumDevelopment(cdo.pacMembers, Some(memberObj.members), cdo.submissionDate, cdo.decision)
                saveCurriculumDevelopmentObject(message.messageId, memberObj.devCode, curDev3, "cur-dev-appoint-cdc-res")
            }
        }
    }

    def handleDraftRevision(message: CurriculumDevelopmentDraftRevisionRequestMessage): Unit = {
        println("handling draft revision...")
        val simpleSuccessRespMsg: SimpleResponseMessage = new SimpleResponseMessage(message.messageId, None, Some("ok"))
        val succMsgStr = Json.toJson(simpleSuccessRespMsg).toString()
        messenger.getProducer().send(new ProducerRecord[String,String]("cur-dev-draft-revise-res", succMsgStr))
    }

    def handleDraftSubmission(message: CurriculumDevelopmentDraftSubmissionRequestMessage): Unit = {
        println("handling draft submission ...")
        val submissionObj = message.content

        val curDevObjRes = DBManager.findCurriculumDevelopmentObject(submissionObj.devCode)
        curDevObjRes.onComplete {
            case Failure(curDevFailure) => {
                println("no curriculum development object exists yet ...")
                val curDev1: CurriculumDevelopment = new CurriculumDevelopment(None, None, Some(submissionObj.submissionDate), None)
                saveCurriculumDevelopmentObject(message.messageId, submissionObj.devCode, curDev1, "cur-dev-draft-submit-res")
            }
            case Success(cdo) => {
                println("a curriculum development object exists already ...")
                val curDev3: CurriculumDevelopment = new CurriculumDevelopment(cdo.pacMembers, cdo.cdcMembers, Some(submissionObj.submissionDate), cdo.decision)
                saveCurriculumDevelopmentObject(message.messageId, submissionObj.devCode, curDev3, "cur-dev-draft-submit-res")
            }
        }
    }

    def handleDraftValidation(message: CurriculumDevelopmentDraftValidationRequestMessage): Unit = {
        println("handling draft validation ...")
        val validationObj = message.content

        val curDevObjRes = DBManager.findCurriculumDevelopmentObject(validationObj.devCode)
        curDevObjRes.onComplete {
            case Failure(curDevFailure) => {
                println("no curriculum development object exists yet ...")
                val curDev1: CurriculumDevelopment = new CurriculumDevelopment(None, None, None, Some(validationObj.decision))
                saveCurriculumDevelopmentObject(message.messageId, validationObj.devCode, curDev1, "cur-dev-draft-validate-res")
            }
            case Success(cdo) => {
                println("a curriculum development object exists already ...")
                val curDev3: CurriculumDevelopment = new CurriculumDevelopment(cdo.pacMembers, cdo.cdcMembers, cdo.submissionDate, Some(validationObj.decision))
                saveCurriculumDevelopmentObject(message.messageId, validationObj.devCode, curDev3, "cur-dev-draft-validate-res")
            }
        }
    }

    def saveCurriculumDevelopmentObject(messageId: String, devCode: String,  curDev: CurriculumDevelopment, respTopic: String): Unit = {
        val addCurDevRes = DBManager.upsertCurriculumDevelopment(devCode, curDev)
        handleInsertionResultWithSimpleResponse(addCurDevRes, messageId, respTopic)
    }
}
