import akka.actor._

abstract class MessageProcessor extends Actor {
    var messenger: YesterProducer = null
}
