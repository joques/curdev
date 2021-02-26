package yester.message.processor

import akka.actor._
import org.apache.kafka.clients.producer.ProducerRecord
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{Reads, Json, Writes}
import java.util.UUID
import yester.YesterProducer
import yester.util.DBManager
import yester.lib.{StartReview, RecommendReview}
import yester.message.request.{StartReviewRequestMessage, RecommendReviewRequestMessage}
import yester.message.response.SimpleResponseMessage

final case class ReviewMessageProcessor(messenger: YesterProducer) extends MessageProcessor(messenger) {
    def receive = {
        case sRevReqMsg: StartReviewRequestMessage => {
            println("received start review message ...")
            startReview(sRevReqMsg)
        }
        case rRevReqMsg: RecommendReviewRequestMessage => {
            println("received recommend review message ...")
            recommendReview(rRevReqMsg)
        }
    }

    def startReview(message: StartReviewRequestMessage): Unit = {
        println("handling start review data ...")
        val simpleSuccessRespMsg: SimpleResponseMessage = new SimpleResponseMessage(message.messageId, None, Some("ok"))
        val succMsgStr = Json.toJson(simpleSuccessRespMsg).toString()
        messenger.getProducer().send(new ProducerRecord[String,String]("review-unit-start-res", succMsgStr))
    }

    def recommendReview(message: RecommendReviewRequestMessage): Unit = {
        println("handling recommend review data ...")
        val simpleSuccessRespMsg: SimpleResponseMessage = new SimpleResponseMessage(message.messageId, None, Some("ok"))
        val succMsgStr = Json.toJson(simpleSuccessRespMsg).toString()
        messenger.getProducer().send(new ProducerRecord[String,String]("review-unit-recommend-res", succMsgStr))
    }
}
