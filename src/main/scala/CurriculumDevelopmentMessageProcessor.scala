import akka.actor._
import org.apache.kafka.clients.producer.ProducerRecord
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{Reads, Json, Writes}
import java.util.UUID

class CurriculumDevelopmentMessageProcessor extends MessageProcessor {
    def receive = {
        case curDevReqMsg: CurriculumReviewRequestMessage =>
            println("received curriculum-review message ...")
            startCurriculumReview(curDevReqMsg)
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
}
