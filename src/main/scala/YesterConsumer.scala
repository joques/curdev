import java.io.Closeable
import java.util.concurrent.{TimeUnit, Executors, ExecutorService}
import org.apache.kafka.clients.consumer.{KafkaConsumer, ConsumerRecords, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import scala.collection.JavaConversions._
import scala.util.{Failure, Success}
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


        println("printing details about the new record -- begin")

        val recordTopic = record.topic()
        val recordKey = record.key()
        val recordValue = record.value()
        val recordOffset = record.offset()

        println(s"topic = $recordTopic")
        println(s"key = $recordKey")
        println(s"value = $recordValue")
        println(s"offset = $recordOffset")

        println("printing details about the new record -- end")
        println("")

        println("delving into the message -- begin")
        val message = Json.parse(recordValue).as[SimpleRequestMessage]
        println(message)

        recordTopic match {
            case "find-users-req" => findUser(message)
            case "create-users-req" => createUser(message)
            case _ => println("unknown topic...")
        }

        println("delving into the message -- end")
    }

    def findUser(message: SimpleRequestMessage): Unit = {
        val userName = message.content
        println(s"finding user $userName")
        val userResult = DBManager.findUser("kunta")
        userResult.onComplete {
            case Success(userVal) => println(s"We got user $userVal")
            case Failure(userErr) => userErr.printStackTrace
        }
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
