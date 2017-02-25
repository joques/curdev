import akka.actor._

case class NeedAnalysisMessageProcessor(messenger: YesterProducer) extends MessageProcessor(messenger) {
    def receive = {
        case "need-analysis-start-req" =>
            println("received need-analysis-start-req message")
    }
}
