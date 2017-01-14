import kafka.consumer.{ Consumer => KafkaConsumer, ConsumerConfig, ConsumerIterator, Whitelist }
import kafka.consumer._
import kafka.serializer.{DefaultDecoder, Decoder}
import scala.collection.JavaConversions._
import kafka.api._
import java.util.Properties

// abstract class Consumer(topics: List[String]) {
//     def read(): Iterable[String]
// }

case class Consumer (topics: List[String]) {
    private val filterSpec = new Whitelist(topics.mkString(","))

    private val props = new Properties()
    props.put("group.id", "1234")
    props.put("zookeeper.connect", "127.0.0.1:2181")
    props.put("auto.offset.reset", "smallest")
    private val config = new ConsumerConfig(props)

    private lazy val consumer = KafkaConsumer.create(config)
    private lazy val stream = consumer.createMessageStreamsByFilter(filterSpec, 1, new DefaultDecoder(), new DefaultDecoder()).get(0)

    def read(): Stream[String] = Stream.cons(new String(stream.head.message), read())

    // def read(writer: (Array[Byte]) => Unit) = {
    //     // read on the stream
    //     for (messageAndTopic <- stream) {
    //         try {
    //             writer(messageAndTopic.message)
    //         }
    //         catch {
    //             case e: Throwable =>
    //                 if (true) {
    //                     sys.error("Error processing message, skipping this message: " + e.toString)
    //                 } else {
    //                     throw e
    //                 }
    //         }
    //     }
    // }
}
