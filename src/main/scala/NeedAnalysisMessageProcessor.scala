import akka.actor._
case class NeedAnalysisMessageProducer extends MessageProcessor {
    def receive = {
        case YesterProducer(yProd) =>
            messenger = yProd
    }
}
