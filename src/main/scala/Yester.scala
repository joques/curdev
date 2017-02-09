// import org.apache.kafka.clients.consumer.{ ConsumerRecords, ConsumerRecord }
// import scala.collection.JavaConversions._

object Yester {
    def main(args: Array[String]) {
        println("welcome to Yester... The resource management micro service...")
        val topicList = List("find-users-req","create-users-req", "summary-req", "need-analysis-start-req")
        val yProducer = new YesterProducer()
        val yConsumer = new YesterConsumer(topicList)

        println("displaying consumer and producer...")
        println(yConsumer.toString)
        println("calling read() from yester...")

        yConsumer.startConsuming(yProducer)
    }
}
