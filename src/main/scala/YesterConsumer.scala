import java.io.Closeable
import java.util.concurrent.{TimeUnit, Executors, ExecutorService}
import org.apache.kafka.clients.consumer.{KafkaConsumer, ConsumerRecords, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import scala.collection.JavaConversions._
import rx.lang.scala.Observable
import java.util.Properties
import java.util.UUID
import play.api.libs.json.{Reads, Json}


case class YesterConsumer (topics: List[String]) extends Closeable with Runnable {
    var consumer: KafkaConsumer[String, String] = null
    val pool: ExecutorService = Executors.newFixedThreadPool(1)
    var shouldRun: Boolean = true
    var messenger: YesterProducer = null
    implicit val reader: Reads[SimpleRequestMessage] = SimpleRequestMessageJsonImplicits.simpleRequestMessageReads

    def startConsuming(producer: YesterProducer) : Unit = {
        messenger = producer
        pool.execute(this)
    }

    override def close(): Unit = {
        shouldRun = false
        shutDownAndAwaitTermination(pool)
    }

    def handleRecord(record: ConsumerRecord[String,String]): Unit = {
        val recordTopic = record.topic()

        println("printing details about the new record -- begin")

        println(s"topic = $record.topic()")
        println(s"key = $record.key()")
        println(s"value = $record.value")
        println(s"offset = $record.offset()")

        val message = Json.parse(record.value()).as[SimpleRequestMessage]
        println("printing message")
        println(message)

        println("printing details about the new record -- end")
        println("")

        recordTopic match {
            case "find-users-req" => findUser(message)
            case "create-users-req" => createUser(message)
            case _ => println("unknown topic...")
        }
    }

    def findUser(message: SimpleRequestMessage): Unit = {
        val userName = message.content
        println(s"finding user $userName")
    }

    def createUser(message: SimpleRequestMessage): Unit = {
        println("creating new user")
    }

    def run() : Unit = {
        try{
            val props = new Properties()
            val groupIDSuffix: String = UUID.randomUUID().toString
            props.put("group.id", s"yester-$groupIDSuffix")
            props.put("bootstrap.servers", "localhost:9092")
            props.put("zookeeper.connect", "localhost:2181")
            props.put("enable.auto.commit", "true")
            props.put("auto.commit.interval.ms", "3000")
            props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
            props.put("session.timeout.ms", "10000")
            props.put("fetch.min.bytes", "50000")
            props.put("max.partition.fetch.bytes", "2097152")
            props.put("auto.offset.reset", "earliest")

            consumer = new KafkaConsumer[String,String](props)
            consumer.subscribe(topics)

            while(shouldRun) {
                println("yester consumer loop -- begin")

                val records: ConsumerRecords[String,String] = consumer.poll(200)
                val itRec = records.iterator()

                while(itRec.hasNext()) {
                    val record: ConsumerRecord[String,String] = itRec.next()
                    handleRecord(record)
                }

                println("yester consumer loop -- end")
            }
        }
        catch {
            case throwable: Throwable =>
                shouldRun = false
                val st = throwable.getStackTrace()
                println(s"got an error $st")
        }
        finally {
            shutDownAndAwaitTermination(pool)
        }
    }

    def shutDownAndAwaitTermination (pool: ExecutorService) : Unit = {
        pool.shutdown()
        try {}
        catch {
            case throwable: Throwable =>
                val st = throwable.getStackTrace()
                println(s"Got an exception $st")
                pool.shutdownNow()
                Thread.currentThread().interrupt()
        }
    }
}