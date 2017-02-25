import akka.actor._
case class NeedAnalysisMessageProcessor extends MessageProcessor {
    def receive = {
        case YesterProducer(yProd) =>
            messenger = yProd
    }
}
