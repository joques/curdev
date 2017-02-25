import akka.actor._

abstract class MessageProcessor(messenger: YesterProducer) extends Actor {}
