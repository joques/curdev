package yester.message.processor

import akka.actor._
import org.apache.kafka.clients.producer.ProducerRecord
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{Reads, Json, Writes}
import java.util.UUID
import yester.YesterProducer
import yester.util.DBManager
mport yester.lib.{Consultation}
import yester.message.request.{ConsultationRequestMessage, ConsultationRequestMessageJsonImplicits}
import yester.message.response.SimpleResponseMessage


final case class ConsultationMessageProcessor(messenger: YesterProducer) extends MessageProcessor(messenger) {
    def receive = {
        case consReqMsg: ConsultationRequestMessage => {
            println("received consultation message ...")
            addConsultations(consReqMsg)
        }
    }
}
