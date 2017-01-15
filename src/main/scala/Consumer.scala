import org.apache.kafka.clients.consumer.{KafkaConsumer, ConsumerRecords}
import org.apache.kafka.common.serialization.StringDeserializer
import scala.collection.JavaConversions._
import kafka.api._
import java.util.Properties

// abstract class Consumer(topics: List[String]) {
//     def read(): Iterable[String]
// }

case class Consumer (topics: List[String]) {
    // private val filterSpec = new Whitelist(topics.mkString(","))

    private val props = new Properties()
    props.put("group.id", "yester-003")
    props.put("bootstrap.servers", "localhost:9092")
    props.put("zookeeper.connect", "localhost:2181")
    props.put("enable.auto.commit", "true")
    props.put("auto.commit.interval.ms", "3000")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    // props.put("session.timeout.ms", "10000")
    props.put("fetch.message.max.bytes", "16777216")
    props.put("auto.offset.reset", "earliest")

    private lazy val consumer: KafkaConsumer[String,String] = new KafkaConsumer(props)
    consumer.subscribe(topics)

    println("listing the topic subscription...")
    println(consumer.subscription())

    def read(): ConsumerRecords[String,String] = {
        println("polling the queue...")
        val polRes: ConsumerRecords[String, String] = consumer.poll(2000)
        polRes
    }
}
