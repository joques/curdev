import java.util.{Properties, UUID}
import kafka.producer.{KeyedMessage, ProducerConfig, Producer => KafkaProducer}
import kafka.message.NoCompressionCodec

case class Producer[A] () {
    val props = new Properties()
    val compressionCodec = NoCompressionCodec.codec
    props.put("compression.codec", compressionCodec.toString)
    props.put("producer.type", "sync")
    props.put("metadata.broker.list", "127.0.0.1:8092")
    props.put("batch.num.messages", 200.toString)
    props.put("message.send.max.retries", 3.toString)
    props.put("request.required.acks", (-1).toString)
    props.put("client.id", UUID.randomUUID().toString)

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
    def apply[T]() = new Producer[T]()
}
