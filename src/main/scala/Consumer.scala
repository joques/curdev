import kafka.consumer.{ Consumer => KafkaConsumer, ConsumerConfig, ConsumerIterator, Whitelist }
import kafka.consumer._
import kafka.message._
import kafka.utils._
import kafka.serializer.{DefaultDecoder, Decoder}
import scala.collection.JavaConversions._
import scala.collection.GenTraversableOnce
import kafka.api._
import java.util.Properties

// abstract class Consumer(topics: List[String]) {
//     def read(): Iterable[String]
// }

case class Consumer (topics: List[String]) {
    private val filterSpec = new Whitelist(topics.mkString(","))

    private val props = new Properties()
    props.put("group.id", "1234")
    props.put("zookeeper.connect", "localhost:2181")
    props.put("auto.offset.reset", "smallest")
    private val config = new ConsumerConfig(props)

    private lazy val consumer = KafkaConsumer.create(config)
    private lazy val consumerMap = consumer.createMessageStreams(Map("find-users-req" -> 1))
    private lazy val stream = consumerMap.getOrElse("find-users-req", List()).head
    def read(): Stream[String] = {
        println("inside consumer read...")
        Stream.cons(new String(stream.head.message), read())
    }

    // def read(writer: (Array[Byte]) => Unit) = {
    //     println("inside consumer.read ....")
    //     println("taking a look at the stream")
    //     println(stream)
    //     println("looking at the head")
    //     println(stream.head)
    //     // read on the stream
    //     for (messageAndTopic <- stream) {
    //         try {
    //             println(messageAndTopic.toString)
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
