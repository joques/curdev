import kafka.consumer.{ Consumer => KafkaConsumer, ConsumerIterator, WhiteList }
import kafka.consumer._
import kafka.serializer.{DefaultDecoder, Decoder}
import scala.collection.JavaConversions._
import kafka.api._

abstract class Consumer(topics: List[String]) {
    protected val kafkaConfig = KafkaConfig()
    protected val config = new ConsumerConfig(kafkaConfig)

    def read(): Iterable[String]
}

case class StreamConsumer (topics: List[String]) extends Consumer(topics) {
    private val filterSpec = new WhiteList(topics.mkString(","))

    protected val keyDecoder: Decoder[Array[Byte]] = new DefaultDecoder()
    protected val valueDecoder: Decoder[Array[Byte]] = new DefaultDecoder()

    private lazy val consumer = KafkaConsumer.create(config)
    private lazy val stream = consumer.createMessageStreamByFilter(filterSpec, 1, keyDecoder, valueDecoder).get(0)

    def read(): Stream[String] = Stream.cons(new String(stream.head.message), read())
}

object StreamConsumer {
    def apply(topics: List[String], kDecoder: Decoder[Array[Byte]], vDecoder: Decoder[Array[Byte]]) = new StreamConsumer(topics) {
        override val keyDecoder = kDecoder
        override val valueDecoder = vDecoder
    }
}