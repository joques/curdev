import kafka.consumer.{ Consumer => KafkaConsumer }
import kafka.consumer._
import kafka.serializer._
import scala.collection.JavaConversions._
import kafka.api._

abstract class Consumer(topics: List[String]) {
    protected val kafkaConfig = kafkaConfig()
    protected val config = new ConsumerConfig(kafkaConfig)

    def read(): Iterable[String]
}
