import java.util.Properties
import kafka.producer.{KeyedMessage, ProducerConfig, Producer => KafkaProducer}

case class Producer[A] () {
    val props = new Properties()
    props.put("metadata.broker.list", "127.0.0.1:")
    protected val config = new ProducerConfig(props)
    private lazy val producer = new KafkaProducer[A, A](config)

    def send(topic: String, message: A) = sendMessage(producer, keyedMessage(topic, message))

    def sendStream(topic: String, stream: Stream[A]) = {
        val iter = stream.iterator
        while(iter.hasNext) {
            send(topic, iter.next())
        }
    }

    private def keyedMessage(topic: String, message: A): KeyedMessage[A, A] = new KeyedMessage[A, A](topic, message)
    private def sendMessage(producer: KafkaProducer[A, A], message: KeyedMessage[A, A]) = producer.send(message)
}

object Producer {
    def apply[T](props: Properties) = new Producer[T]() {
        override val config = new ProducerConfig(props)
    }
}
