package yester.message.processor

import akka.actor._
import org.apache.kafka.clients.producer.ProducerRecord
import scala.util.{Failure, Success}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{Reads, Json, Writes}

import com.couchbase.client.scala.kv.MutationResult

import yester.YesterProducer
import yester.lib.{User, UserJsonImplicits, PreProgrammeComponent,  Programme, ProgrammeJsonImplicits, NeedAnalysis, NeedAnalysisJsonImplicits, CurriculumDevelopment, CurriculumDevelopmentJsonImplicits}
import yester.message.response.{SimpleResponseMessage, SummaryResponseMessage, SummaryResponseMessageJsonImplicits, SimpleResponseMessageJsonImplicits}

abstract class MessageProcessor(messenger: YesterProducer) extends Actor {

    implicit val summaryRespWriter: Writes[SummaryResponseMessage] = SummaryResponseMessageJsonImplicits.summaryResponseMessageWrites
    implicit val simpleRespWriter: Writes[SimpleResponseMessage] = SimpleResponseMessageJsonImplicits.simpleResponseMessageWrites


    def handleInsertionResultWithSimpleResponse(result: Future[MutationResult], messageId: String, responseTopic: String): Unit = {
        result.onComplete {
            case Success(succRes) => {
				val simpleSuccessRespMsg: SimpleResponseMessage = new SimpleResponseMessage(messageId, None, Some("Insertion operation successful..."))
				val succMsgStr = Json.toJson(simpleSuccessRespMsg).toString()
                println(s"the success message to be sent is $succMsgStr")
                messenger.getProducer().send(new ProducerRecord[String,String](responseTopic, succMsgStr))
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
