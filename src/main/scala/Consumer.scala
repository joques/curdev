import kafka.consumer.{ Consumer => KafkaConsumer, ConsumerConfig, ConsumerIterator, Whitelist }
import kafka.consumer._
import kafka.serializer.{DefaultDecoder, Decoder}
import scala.collection.JavaConverters._
import kafka.api._
import java.util.Properties

abstract class Consumer(topics: List[String]) {
    val props = new Properties()
    props.put("group.id", "1234")
    props.put("zookeeper.connect", "127.0.0.1:2181")
    props.put("auto.offset.reset", "smallest")
    protected val config = new ConsumerConfig(props)


    def read(): Iterable[String]
}

case class StreamConsumer (topics: List[String]) extends Consumer(topics) {
    private val filterSpec = new Whitelist(topics.mkString(","))

    protected val keyDecoder: Decoder[Array[Byte]] = new DefaultDecoder()
    protected val valueDecoder: Decoder[Array[Byte]] = new DefaultDecoder()

    private lazy val consumer = KafkaConsumer.create(config)
    private lazy val stream = consumer.createMessageStreamsByFilter(filterSpec, 1, keyDecoder, valueDecoder)(0)

    // def read(): Stream[String] = Stream.cons(new String(stream.head.message), read())

    def read() = {
        // read on the stream
        for (messageAndTopic <- stream) {
            try {
                println(messageAndTopic.message)
            }
            catch {
                case e: Throwable =>
                    if (true) {
                        error("Error processing message, skipping this message: " + e.toString)
                    } else {
                        throw e
                    }
            }
        }
    }
}

object StreamConsumer {
    def apply(topics: List[String], kDecoder: Decoder[Array[Byte]], vDecoder: Decoder[Array[Byte]]) = new StreamConsumer(topics) {
        override val keyDecoder = kDecoder
        override val valueDecoder = vDecoder
    }
}
