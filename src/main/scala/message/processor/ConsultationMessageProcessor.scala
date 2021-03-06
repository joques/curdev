package yester.message.processor

import akka.actor._
import org.apache.kafka.clients.producer.ProducerRecord
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{Reads, Json, Writes}
import java.util.UUID
import yester.YesterProducer
import yester.util.DBManager
import yester.lib.{Consultation, Benchmark, FinalDraft, Endorsement}
import yester.message.request.{ConsultationRequestMessage, BenchmarkRequestMessage, FinalDraftRequestMessage, EndorsementRequestMessage}
import yester.message.response.SimpleResponseMessage

final case class ConsultationMessageProcessor(messenger: YesterProducer) extends MessageProcessor(messenger) {
    def receive = {
        case consReqMsg: ConsultationRequestMessage => {
            println("received consultation message ...")
            addConsultations(consReqMsg)
        }
        case benchmarkReqMsg: BenchmarkRequestMessage => {
            println("received benchmark message ...")
            addBenchmark(benchmarkReqMsg)
        }
        case fdReqMsg: FinalDraftRequestMessage => {
            println("received final draft request ...")
            addFinalDraft(fdReqMsg)
        }
        case endReqMsg: EndorsementRequestMessage => {
            println("received endorsement request ...")
            addEndorsement(endReqMsg)
        }
    }

    def addConsultations(message: ConsultationRequestMessage): Unit  = {
        println("handling consultation data ...")
        val simpleSuccessRespMsg: SimpleResponseMessage = new SimpleResponseMessage(message.messageId, None, Some("ok"))
        val succMsgStr = Json.toJson(simpleSuccessRespMsg).toString()
        messenger.getProducer().send(new ProducerRecord[String,String]("consult-start-pac-res", succMsgStr))
    }

    def addBenchmark(message: BenchmarkRequestMessage): Unit = {
        println("handling benchmark data ...")
        val simpleSuccessRespMsg: SimpleResponseMessage = new SimpleResponseMessage(message.messageId, None, Some("ok"))
        val succMsgStr = Json.toJson(simpleSuccessRespMsg).toString()
        messenger.getProducer().send(new ProducerRecord[String,String]("consult-benchmark-res", succMsgStr))
    }

    def addFinalDraft(message: FinalDraftRequestMessage): Unit = {
        println("handling final draft data ...")
        val simpleSuccessRespMsg: SimpleResponseMessage = new SimpleResponseMessage(message.messageId, None, Some("ok"))
        val succMsgStr = Json.toJson(simpleSuccessRespMsg).toString()
        messenger.getProducer().send(new ProducerRecord[String,String]("consult-final-draft-res", succMsgStr))
    }

    def addEndorsement(message: EndorsementRequestMessage): Unit = {
        println("handling endorsement data ...")
        val simpleSuccessRespMsg: SimpleResponseMessage = new SimpleResponseMessage(message.messageId, None, Some("ok"))
        val succMsgStr = Json.toJson(simpleSuccessRespMsg).toString()
        messenger.getProducer().send(new ProducerRecord[String,String]("consult-endorse-res", succMsgStr))
    }
}
