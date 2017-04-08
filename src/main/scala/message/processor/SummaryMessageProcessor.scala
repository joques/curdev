package yester.message.processor

import akka.actor._
import org.apache.kafka.clients.producer.ProducerRecord
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{Reads, Json, Writes}
import java.util.UUID
import io.lamma._

import yester.YesterProducer
import yester.util.DBManager
import yester.message.request.SimpleRequestMessage
import yester.message.response.{SummaryResponseMessage, SummaryResponseMessageJsonImplicits}
import yester.lib.{Programme, Summary}

final case class SummaryMessageProcessor(messenger: YesterProducer) extends MessageProcessor(messenger) {

    def receive = {
        case sumReqMsg: SimpleRequestMessage =>
            println("received summary-req message ...")
            getSummary(sumReqMsg)
        case _ =>
            println("unknown message type ...")
    }

    def getSummary(message: SimpleRequestMessage): Unit = {
        println("getting summary...")
        val summaryType = message.content

        summaryType match {
            case "short-summary" => getShortSummary(message.messageId)
            case "full-summary" => getFullSummary(message.messageId)
            case _ => println("unknown command...")
        }
    }

    def getShortSummary(messageId: String): Unit = {
        println("processing short summary...")

        val allProgs = DBManager.findAllProgrammes()
        allProgs.onComplete {
            case (Success(progs)) => {
                println(s"the list is: $progList")

                val inProgress: Seq[Programme] = progs.filter((prg: Programme) => prg.isPreProgramme).take(5)
                println(s"the in progress list is $inProgress")


                val now = Date.today()
                val inSixMonth = now + (6 months)
                val threeMonthsAgo = now - (3 months)
                println(s"the time now is $now and in six months is $inSixMonth")

                val durForReview: Seq[Programme] = for {
                    curProg <- progs
                    if ((!curProg.isPreProgramme) && (Date(curProg.progComponent.get.nextReview) <= inSixMonth))
                } yield curProg
                println(s"due for review list $durForReview")

                val recentlyApproved: Seq[Programme] = for {
                    curProg1 <- progs
                    if ((!curProg1.isPreProgramme) && (threeMonthsAgo <= Date(curProg1.progComponent.get.approvedOn)))
                } yield curProg1
                println(s"recently approved list $recentlyApproved")

                val summary = new Summary(Option(inProgress), Option(durForReview), Option(recentlyApproved))
                val summaryRespMsg: SummaryResponseMessage = new SummaryResponseMessage(messageId, None, Option(summary))

                val summaryRespMsgStr = Json.toJson(summaryRespMsg).toString()
                println(s"the  message to be sent is $summaryRespMsgStr")
                messenger.getProducer().send(new ProducerRecord[String,String]("summary-res", summaryRespMsgStr))
            }
            case (Failure(progErr)) => {
                progErr.printStackTrace
                val progErrorRespMsg: SummaryResponseMessage = new SummaryResponseMessage(messageId, Option(progErr.getMessage), None)
                val errMsgStr = Json.toJson(progErrorRespMsg).toString
                println(s"the error message to be sent for all progs is $errMsgStr")
            }
        }
    }

    def getFullSummary(messageId: String): Unit = {
        println("printing full summary...")
    }
}
