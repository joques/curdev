import org.apache.kafka.clients.consumer.{ ConsumerRecords }

object Yester {
    def main(args: Array[String]) {
        println("welcome to Yester... The resource management micro service...")
        val topicList = List("find-users-req", "create-users-req")
        val yProducer = new Producer[String]()
        val yConsumer = new Consumer(topicList)
        println("displaying consumer and producer...")
        println(yConsumer.toString)
        val records: ConsumerRecords[String, String] = yConsumer.read()
        val recordIter = records.iterator()
        val singleRecord = recordIter.next()
        println(singleRecord.key())
        println(singleRecord.value())
        println(singleRecord.offset())
        // for (singleRecord <- records) {
        //     println(singleRecord.key())
        //     println(singleRecord.value())
        //     println(singleRecord.offset())
        // }
    }
}
