import java.io.Closeable
import java.util.concurrent.{TimeUnit, Executors, ExecutorService}
import org.apache.kafka.clients.consumer.{KafkaConsumer, ConsumerRecords, ConsumerRecord}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import scala.collection.JavaConversions._
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import io.lamma._
import rx.lang.scala.Observable
import java.util.Properties
import java.util.UUID
import play.api.libs.json.{Reads, Json, Writes}


case class YesterConsumer (topics: List[String]) extends Closeable with Runnable {
    var consumer: KafkaConsumer[String, String] = null
    val pool: ExecutorService = Executors.newFixedThreadPool(1)
    var shouldRun: Boolean = true
    var messenger: YesterProducer = null

    implicit val reqReader: Reads[SimpleRequestMessage] = SimpleRequestMessageJsonImplicits.simpleRequestMessageReads
    implicit val pReqReader: Reads[ProgrammeRequestMessage] = ProgrammeRequestMessageJsonImplicits.programmeRequestMessageReads
    implicit val nacReqReader: Reads[NeedAnalysisConsultationRequestMessage] = NeedAnalysisConsultationRequestMessageJsonImplicits.needAnaConsRequestMessageReads
    implicit val userRespWriter: Writes[UserResponseMessage] = UserResponseMessageJsonImplicits.userResponseMessageWrites
    implicit val userWPRespWriter: Writes[UserWithPreProgrammeResponseMessage] = UserResponseMessageJsonImplicits.uwPPResponseMessageWrites
    implicit val summaryRespWriter: Writes[SummaryResponseMessage] = SummaryResponseMessageJsonImplicits.summaryResponseMessageWrites
    implicit val simpleRespWriter: Writes[SimpleResponseMessage] = SimpleResponseMessageJsonImplicits.simpleResponseMessageWrites

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
            case "find-users-req" => {
                val findUserMessage = Json.parse(recordValue).as[SimpleRequestMessage]
                findUser(findUserMessage)
            }
            case "create-users-req" => {
                val createUserMessage = Json.parse(recordValue).as[SimpleRequestMessage]
                createUser(createUserMessage)
            }
            case "summary-req" => {
                val summaryMessage = Json.parse(recordValue).as[SimpleRequestMessage]
                getSummary(summaryMessage)
            }
            case "need-analysis-start-req" => {
                val needAnalysisStartMessage = Json.parse(recordValue).as[ProgrammeRequestMessage]
                createPreProgramme(needAnalysisStartMessage)
            }
            case "need-analysis-consult-req" => {
                val needAnalysisConsultMessage = Json.parse(recordValue).as[NeedAnalysisConsultationRequestMessage]
                addNeedAnalysisConsultation(needAnalysisConsultMessage)
            }
            case _ => println("unknown topic ...")
        }

        println("delving into the message -- end")
    }

    def findUser(message: SimpleRequestMessage): Unit = {
        val userName = message.content
        println(s"finding user $userName")
        val userResult = DBManager.findUser(userName)
        userResult.onComplete {
            case Success(userVal) => {
                println(s"We got user $userVal")
                val userSuccessRespMsg: UserResponseMessage = new UserResponseMessage(message.messageId, None, userVal.get)
                val succMsgStr = Json.toJson(userSuccessRespMsg).toString()
                println(s"the success message to be sent is $succMsgStr")
                messenger.getProducer().send(new ProducerRecord[String,String]("find-users-res", succMsgStr))
            }
            case Failure(userErr) => {
                userErr.printStackTrace
                val userErrorRespMsg: UserResponseMessage = new UserResponseMessage(message.messageId, Option(userErr.getMessage), None)
                val errMsgStr = Json.toJson(userErrorRespMsg).toString()
                println(s"the error message to be sent it $errMsgStr")
                messenger.getProducer().send(new ProducerRecord[String,String]("find-users-res", errMsgStr))
            }
        }
    }

    def findUserWithPreProgramme(message: SimpleRequestMessage): Unit = {
        val userName = message.content
        println(s"finding user $userName")
        val userResult = DBManager.findUser(userName)
        userResult.onComplete {
            case Success(userVal) => {
                println(s"We got user $userVal")

                println("Will now look for pre programmes")

                val allProgs = DBManager.findAllProgrammes()

                allProgs.onComplete {
                    case Failure(progError) => {
                        val progListErrorRespMsg: UserWithPreProgrammeResponseMessage = new UserWithPreProgrammeResponseMessage(message.messageId, Option(progError.getMessage), None)
                        val errMsgStr1 = Json.toJson(progListErrorRespMsg).toString()
                        println(s"the error message to be sent is $errMsgStr1")
                        messenger.getProducer().send(new ProducerRecord[String,String]("find-users-res", errMsgStr1))
                    }
                    case Success(progList) => {
                        val preProgrammeList: List[Programme] = progList.filter((prg: Programme) => prg.isPreProgramme)
                        var preProgCodes: List[String] = for (prg1 <- preProgrammeList if prg1.preProgComponent.get.initiator == "userName") yield prg1.preProgComponent.get.devCode
                        var userWPrePrg: Option[UserWithPreProgramme] = None
                        if (preProgCodes.isEmpty) {
                            userWPrePrg = Some(new UserWithPreProgramme(userVal.get, None))
                        }
                        else {
                            userWPrePrg = Some(new UserWithPreProgramme(userVal.get, Option(preProgCodes)))
                        }
                        val succRespMsg: UserWithPreProgrammeResponseMessage = new UserWithPreProgrammeResponseMessage(message.messageId, None, userWPrePrg)
                        val succMsgStr = Json.toJson(succRespMsg).toString()
                        println(s"the success message to be sent is $succMsgStr")
                        messenger.getProducer().send(new ProducerRecord[String,String]("find-users-res", succMsgStr))
                    }
                }
            }
            case Failure(userErr) => {
                userErr.printStackTrace
                val userErrorRespMsg: UserWithPreProgrammeResponseMessage = new UserWithPreProgrammeResponseMessage(message.messageId, Option(userErr.getMessage), None)
                val errMsgStr = Json.toJson(userErrorRespMsg).toString()
                println(s"the error message to be sent it $errMsgStr")
                messenger.getProducer().send(new ProducerRecord[String,String]("find-users-res", errMsgStr))
            }
        }
    }

    def createUser(message: SimpleRequestMessage): Unit = {
        println("creating new user...")
    }

    def createPreProgramme(message: ProgrammeRequestMessage): Unit = {
        println("creating a new programme object ...")

        val progObj = message.content
        val progKey = UUID.randomUUID().toString()
        val createProgOpRes = DBManager.createProgramme(progKey, progObj)
        createProgOpRes.onComplete {
            case Success(createPreProgSuccOpRes) => {
                if (createPreProgSuccOpRes.isSuccess) {
                    val simpleSuccessRespMsg: SimpleResponseMessage = new SimpleResponseMessage(message.messageId, None, Option(createPreProgSuccOpRes.getMessage))
                    val succMsgStr = Json.toJson(simpleSuccessRespMsg).toString()
                    println(s"the success message to be sent is $succMsgStr")
                    messenger.getProducer().send(new ProducerRecord[String,String]("need-analysis-start-res", succMsgStr))
                }
                else {
                    val simpleErrorRespMsg1: SimpleResponseMessage = new SimpleResponseMessage(message.messageId, Option(createPreProgSuccOpRes.getMessage), None)
                    val errMsgStr1 = Json.toJson(simpleErrorRespMsg1).toString()
                    println(s"the error message to be sent out is $errMsgStr1")
                    messenger.getProducer().send(new ProducerRecord[String,String]("need-analysis-start-res", errMsgStr1))
                }
            }
            case Failure(createPreProgFailOpRes) => {
                val simpleErrorRespMsg: SimpleResponseMessage = new SimpleResponseMessage(message.messageId, Option(createPreProgFailOpRes.getMessage), None)
                val errMsgStr = Json.toJson(simpleErrorRespMsg).toString()
                println(s"the error message to be sent out is $errMsgStr")
                messenger.getProducer().send(new ProducerRecord[String,String]("need-analysis-start-res", errMsgStr))
            }
        }
    }

    def addNeedAnalysisConsultation(message: NeedAnalysisConsultationRequestMessage): Unit = {
        println("adding consultation record for need analysis")

        val consultationObj = message.content
        val consulationKey = UUID.randomUUID().toString()

        val addConsultationOpRes = DBManager.addNeedAnalysisConsultation(consulationKey, consultationObj)

        addConsultationOpRes.onComplete {
            case Success(addConsultationSuccOpRes) => {
                if (addConsultationSuccOpRes.isSuccess) {
                    val simpleSuccessRespMsg: SimpleResponseMessage = new SimpleResponseMessage(message.messageId, None, Option(addConsultationSuccOpRes.getMessage))
                    val succMsgStr = Json.toJson(simpleSuccessRespMsg).toString()
                    println(s"the success message to be sent is $succMsgStr")
                    messenger.getProducer().send(new ProducerRecord[String,String]("need-analysis-consult-res", succMsgStr))
                }
                else {
                    val simpleErrorRespMsg1: SimpleResponseMessage = new SimpleResponseMessage(message.messageId, Option(addConsultationSuccOpRes.getMessage), None)
                    val errMsgStr1 = Json.toJson(simpleErrorRespMsg1).toString()
                    println(s"the error message to be sent out is $errMsgStr1")
                    messenger.getProducer().send(new ProducerRecord[String,String]("need-analysis-consult-res", errMsgStr1))
                }
            }
            case Failure(addConsultationFailOpRes) => {
                val simpleErrorRespMsg: SimpleResponseMessage = new SimpleResponseMessage(message.messageId, Option(addConsultationFailOpRes.getMessage), None)
                val errMsgStr = Json.toJson(simpleErrorRespMsg).toString()
                println(s"the error message to be sent out is $errMsgStr")
                messenger.getProducer().send(new ProducerRecord[String,String]("need-analysis-consult-res", errMsgStr))
            }
        }
    }

    def getSummary(message: SimpleRequestMessage): Unit = {
        println("getting summary...")
        val summaryType = message.content

        summaryType match {
            case "short-summary" => getShortSummary(message.messageId)
            case "full-summary" => getFullSummary(message.messageId)
            case _ => println("unknown command...")
        }
    }

    def getShortSummary(messageId: String): Unit = {
        println("processing short summary...")

        val allProgs = DBManager.findAllProgrammes()
        allProgs.onComplete {
            case (Success(progList)) => {
                println(s"the list is: $progList")

                val inProgress: List[Programme] = progList.filter((prg: Programme) => prg.isPreProgramme).take(5)
                println(s"the in progress list is $inProgress")


                val now = Date.today()
                val inSixMonth = now + (6 months)
                val threeMonthsAgo = now - (3 months)
                println(s"the time now is $now and in six months is $inSixMonth")

                val durForReview: List[Programme] = for {
                    curProg <- progList
                    if ((!curProg.isPreProgramme) && (Date(curProg.progComponent.get.nextReview) <= inSixMonth))
                } yield curProg
                println(s"due for review list $durForReview")

                val recentlyApproved: List[Programme] = for {
                    curProg1 <- progList
                    if ((!curProg1.isPreProgramme) && (threeMonthsAgo <= Date(curProg1.progComponent.get.approvedOn)))
                } yield curProg1
                println(s"recently approved list $recentlyApproved")

                val summary = new Summary(Option(inProgress), Option(durForReview), Option(recentlyApproved))
                val summaryRespMsg: SummaryResponseMessage = new SummaryResponseMessage(messageId, None, Option(summary))

                val summaryRespMsgStr = Json.toJson(summaryRespMsg).toString()
                println(s"the  message to be sent is $summaryRespMsgStr")
                messenger.getProducer().send(new ProducerRecord[String,String]("summary-res", summaryRespMsgStr))
            }
            case (Failure(progErr)) => {
                progErr.printStackTrace
                val progErrorRespMsg: SummaryResponseMessage = new SummaryResponseMessage(messageId, Option(progErr.getMessage), None)
                val errMsgStr = Json.toJson(progErrorRespMsg).toString
                println(s"the error message to be sent for all progs is $errMsgStr")
            }
        }
    }

    def getFullSummary(messageId: String): Unit = {
        println("printing full summary...")
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
