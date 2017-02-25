import akka.actor._

class NeedAnalysisMessageProcessor extends MessageProcessor {
    def receive = {
        case YesterProducer(yProd) =>
            messenger = yProd
    }
}
