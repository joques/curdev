import org.apache.kafka.clients.consumer.{ ConsumerRecords, ConsumerRecord }
import scala.collection.JavaConversions._

object Yester {
    def main(args: Array[String]) {
        println("welcome to Yester... The resource management micro service...")
        val topicList = List("find-users-req", "create-users-req")
        val yProducer = new Producer[String]()
        val yConsumer = new Consumer(topicList)
        println("displaying consumer and producer...")
        println(yConsumer.toString)
        println("calling read() from yester...")
        val records: ConsumerRecords[String, String] = yConsumer.read()
        println("now let's dig in...")
        println(records.count())
        val recordIter: Iterator[ConsumerRecord[String,String]] = records.records("find-users-req").iterator()
        while(recordIter.hasNext()) {
            val singleRecord = recordIter.next()
            println("showing content of current element...")
            println(singleRecord.key())
            println(singleRecord.value())
            println(singleRecord.offset())
        }
    }
}
