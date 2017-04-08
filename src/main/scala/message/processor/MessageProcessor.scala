package yester.message.processor

import akka.actor._
import org.apache.kafka.clients.producer.ProducerRecord
// import org.reactivecouchbase.client.OpResult
import scala.util.{Failure, Success}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{Reads, Json, Writes}

import yester.YesterProducer
import yester.lib.{PreProgrammeComponent, Programme}
import yester.message.response.{SimpleResponseMessage, SummaryResponseMessage, SummaryResponseMessageJsonImplicits, SimpleResponseMessageJsonImplicits}

abstract class MessageProcessor(messenger: YesterProducer) extends Actor {

    implicit val summaryRespWriter: Writes[SummaryResponseMessage] = SummaryResponseMessageJsonImplicits.summaryResponseMessageWrites
    implicit val simpleRespWriter: Writes[SimpleResponseMessage] = SimpleResponseMessageJsonImplicits.simpleResponseMessageWrites


    def handleInsertionResultWithSimpleResponse[T](result: Future[T], messageId: String, responseTopic: String): Unit = {
        result.onComplete {
            case Success(resultData) => {
                val opSucMsg: Option[String] = Option("Object creation successful!")
                val simpleSuccessRespMsg: SimpleResponseMessage = new SimpleResponseMessage(messageId, None, opSucMsg)
                val succMsgStr = Json.toJson(simpleSuccessRespMsg).toString()
                println(s"the success message to be sent is $succMsgStr")
                messenger.getProducer().send(new ProducerRecord[String,String](responseTopic, succMsgStr))
            }
            case Failure(failOpRes) => {
                val opErrMsg: Option[String] = Option("Object creation failed!")
                val simpleErrorRespMsg: SimpleResponseMessage = new SimpleResponseMessage(messageId, opErrMsg, None)
                val errMsgStr = Json.toJson(simpleErrorRespMsg).toString()
                println(s"the error message to be sent out is $errMsgStr")
                messenger.getProducer().send(new ProducerRecord[String,String](responseTopic, errMsgStr))
            }
        }
    }

    def provideResposeToSubmission(resp: String, messageId: String, responseTopic: String): Unit = {
        val simpleRespMsg: SimpleResponseMessage = new SimpleResponseMessage(messageId, None, Option(resp))
        val msgStr = Json.toJson(simpleRespMsg).toString()
        println(s"the message to be sent is $msgStr")
        messenger.getProducer().send(new ProducerRecord[String,String](responseTopic, msgStr))
    }
}
