import akka.actor._
import java.io.Closeable
import java.util.concurrent.{TimeUnit, Executors, ExecutorService}
import org.apache.kafka.clients.consumer.{KafkaConsumer, ConsumerRecords, ConsumerRecord}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import scala.collection.JavaConversions._
import scala.util.{Failure, Success}
import org.reactivecouchbase.client.OpResult
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import io.lamma._
import rx.lang.scala.Observable
import java.util.Properties
import java.util.UUID
import play.api.libs.json.{Reads, Json, Writes}


final case class YesterConsumer (topics: List[String]) extends Closeable with Runnable {
    var consumer: KafkaConsumer[String, String] = null
    val pool: ExecutorService = Executors.newFixedThreadPool(1)
    var shouldRun: Boolean = true
    var actorMap: Map[String, ActorRef] = null

    implicit val reqReader: Reads[SimpleRequestMessage] = SimpleRequestMessageJsonImplicits.simpleRequestMessageReads
    implicit val pReqReader: Reads[ProgrammeRequestMessage] = ProgrammeRequestMessageJsonImplicits.programmeRequestMessageReads
    implicit val nacReqReader: Reads[NeedAnalysisConsultationRequestMessage] = NeedAnalysisConsultationRequestMessageJsonImplicits.needAnaConsRequestMessageReads
    implicit val crvReqReader: Reads[CurriculumReviewRequestMessage] = CurriculumReviewRequestMessageJsonImplicits.crvRequestMessageReads
    implicit val fuReqReader: Reads[FindUserRequestMessage] = FindUserRequestMessageJsonImplicits.fuRequestMessageReads
    implicit val cuReqReader: Reads[CreateUserRequestMessage] = CreateUserRequestMessageJsonImplicits.cuRequestMessageReads

    def startConsuming(actors: Map[String, ActorRef]) : Unit = {
        actorMap = actors
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
            case "find-users-req" => {
                val smpMsg = Json.parse(recordValue).as[SimpleRequestMessage]
                val findUserMessage = new FindUserRequestMessage(smpMsg)
                actorMap("user") ! findUserMessage
                // findUser(findUserMessage)
            }
            case "create-users-req" => {
                val smpMsg1 = Json.parse(recordValue).as[SimpleRequestMessage]
                val createUserMessage = new CreateUserRequestMessage(smpMsg1)
                actorMap("user") ! createUserMessage
                // createUser(createUserMessage)
            }
            case "summary-req" => {
                val summaryMessage = Json.parse(recordValue).as[SimpleRequestMessage]
                // getSummary(summaryMessage)
                actorMap("summary") ! summaryMessage
            }
            case "need-analysis-start-req" => {
                val needAnalysisStartMessage = Json.parse(recordValue).as[ProgrammeRequestMessage]
                actorMap("need-analysis") ! needAnalysisStartMessage
                // createPreProgramme(needAnalysisStartMessage)
            }
            case "need-analysis-consult-req" => {
                val needAnalysisConsultMessage = Json.parse(recordValue).as[NeedAnalysisConsultationRequestMessage]
                actorMap("need-analysis") ! needAnalysisConsultMessage
                // addNeedAnalysisConsultation(needAnalysisConsultMessage)
            }
            case "curriculum-review-req" => {
                val curriculumReviewMessage = Json.parse(recordValue).as[CurriculumReviewRequestMessage]
                actorMap("curriculum-development") ! curriculumReviewMessage
                // startCurriculumReview(curriculumReviewMessage)
            }
            case _ => println("unknown topic ...")
        }

        println("delving into the message -- end")
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
            props.put("auto.offset.reset", "latest")

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
