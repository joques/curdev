package yester.message.processor

import akka.actor._
import org.apache.kafka.clients.producer.ProducerRecord
import org.reactivecouchbase.client.OpResult
import scala.util.{Failure, Success}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{Reads, Json, Writes}

import yester.YesterProducer
import yester.lib.{PreProgrammeComponent, Programme}
import yester.message.response.{SimpleResponseMessage, SummaryResponseMessageJsonImplicits, SimpleResponseMessageJsonImplicits}

abstract class MessageProcessor(messenger: YesterProducer) extends Actor {

    implicit val summaryRespWriter: Writes[SummaryResponseMessage] = SummaryResponseMessageJsonImplicits.summaryResponseMessageWrites
    implicit val simpleRespWriter: Writes[SimpleResponseMessage] = SimpleResponseMessageJsonImplicits.simpleResponseMessageWrites


    def handleInsertionResultWithSimpleResponse(result: Future[OpResult], messageId: String, responseTopic: String): Unit = {
        result.onComplete {
            case Success(succOpRes) => {
                if (succOpRes.isSuccess) {
                    val simpleSuccessRespMsg: SimpleResponseMessage = new SimpleResponseMessage(messageId, None, Option(succOpRes.getMessage))
                    val succMsgStr = Json.toJson(simpleSuccessRespMsg).toString()
                    println(s"the success message to be sent is $succMsgStr")
                    messenger.getProducer().send(new ProducerRecord[String,String](responseTopic, succMsgStr))
                }
                else {
                    val simpleErrorRespMsg1: SimpleResponseMessage = new SimpleResponseMessage(messageId, Option(succOpRes.getMessage), None)
                    val errMsgStr1 = Json.toJson(simpleErrorRespMsg1).toString()
                    println(s"the error message to be sent out is $errMsgStr1")
                    messenger.getProducer().send(new ProducerRecord[String,String](responseTopic, errMsgStr1))
                }
            }
            case Failure(failOpRes) => {
                val simpleErrorRespMsg: SimpleResponseMessage = new SimpleResponseMessage(messageId, Option(failOpRes.getMessage), None)
                val errMsgStr = Json.toJson(simpleErrorRespMsg).toString()
                println(s"the error message to be sent out is $errMsgStr")
                messenger.getProducer().send(new ProducerRecord[String,String](responseTopic, errMsgStr))
            }
        }
    }
}
