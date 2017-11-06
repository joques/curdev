package yester

import akka.actor._
import java.io.Closeable
import java.util.concurrent.{TimeUnit, Executors, ExecutorService}
import org.apache.kafka.clients.consumer.{KafkaConsumer, ConsumerRecords, ConsumerRecord}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import scala.collection.JavaConversions._
import scala.util.{Failure, Success}
// import org.reactivecouchbase.client.OpResult
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import io.lamma._
import rx.lang.scala.Observable
import java.util.Properties
import java.util.UUID
import play.api.libs.json.{Reads, Json, Writes}
import yester.message.request.{SimpleRequestMessage, SimpleRequestMessageJsonImplicits, ProgrammeRequestMessage, ProgrammeRequestMessageJsonImplicits,
    NeedAnalysisConsultationRequestMessage, NeedAnalysisConsultationRequestMessageJsonImplicits, NeedAnalysisSurveyRequestMessage,
    NeedAnalysisSurveyRequestMessageJsonImplicits, NeedAnalysisConcludeRequestMessage, NeedAnalysisConcludeRequestMessageJsonImplicits,
    NeedAnalysisBosStartRequestMessage, NeedAnalysisBosStartRequestMessageJsonImplicits, NeedAnalysisBosRecommendRequestMessage,
    NeedAnalysisBosRecommendRequestMessageJsonImplicits, NeedAnalysisSenateRecommendRequestMessage, NeedAnalysisSenateRecommendRequestMessageJsonImplicits,
    NeedAnalysisSenateStartRequestMessage, NeedAnalysisSenateStartRequestMessageJsonImplicits, CurriculumReviewRequestMessage, CurriculumReviewRequestMessageJsonImplicits,
    FindUserRequestMessage, FindUserRequestMessageJsonImplicits, CreateUserRequestMessage, CreateUserRequestMessageJsonImplicits, CurriculumDevelopmentAuthorizationRequestMessage,
    CurriculumDevelopmentAuthorizationRequestMessageJsonImplicits, CommitteeMembersRequestMessage, CommitteeMembersJsonImplicitsRequestMessageJsonImplicits,
    CurriculumDevelopmentAppointPACRequestMessage, CurriculumDevelopmentAppointPACRequestMessageJsonImplicits, CurriculumDevelopmentDraftRevisionRequestMessage,
    CurriculumDevelopmentDraftRevisionRequestMessageJsonImplicits, CurriculumDevelopmentDraftSubmissionRequestMessage,
    CurriculumDevelopmentDraftSubmissionRequestMessageJsonImplicits, CurriculumDevelopmentDraftValidationRequestMessage,
    CurriculumDevelopmentDraftValidationRequestMessageJsonImplicits, ConsultationRequestMessage, ConsultationRequestMessageJsonImplicits,
    BenchmarkRequestMessage, BenchmarkRequestMessageJsonImplicits, CurriculumDevelopmentAppointCDCRequestMessage,
    CurriculumDevelopmentAppointCDCRequestMessageJsonImplicits, FinalDraftRequestMessage, FinalDraftRequestMessageJsonImplicits, EndorsementRequestMessage,
    EndorsementRequestMessageJsonImplicits
}


final case class YesterConsumer (topics: List[String]) extends Closeable with Runnable {
    var consumer: KafkaConsumer[String, String] = null
    val pool: ExecutorService = Executors.newFixedThreadPool(1)
    var shouldRun: Boolean = true
    var actorMap: Map[String, ActorRef] = null

    implicit val reqReader: Reads[SimpleRequestMessage] = SimpleRequestMessageJsonImplicits.simpleRequestMessageReads
    implicit val pReqReader: Reads[ProgrammeRequestMessage] = ProgrammeRequestMessageJsonImplicits.programmeRequestMessageReads
    implicit val nacReqReader: Reads[NeedAnalysisConsultationRequestMessage] = NeedAnalysisConsultationRequestMessageJsonImplicits.needAnaConsRequestMessageReads
    implicit val nasReqReader: Reads[NeedAnalysisSurveyRequestMessage] = NeedAnalysisSurveyRequestMessageJsonImplicits.needAnaSurvRequestMessageReads
    implicit val naclReqReader: Reads[NeedAnalysisConcludeRequestMessage] = NeedAnalysisConcludeRequestMessageJsonImplicits.needAnaConclRequestMessageReads
    implicit val nabsReqReader: Reads[NeedAnalysisBosStartRequestMessage] = NeedAnalysisBosStartRequestMessageJsonImplicits.needAnaBSRequestMessageReads
    implicit val nassReqReader: Reads[NeedAnalysisSenateStartRequestMessage] = NeedAnalysisSenateStartRequestMessageJsonImplicits.needAnaSSRequestMessageReads
    implicit val nabrReqReader: Reads[NeedAnalysisBosRecommendRequestMessage] = NeedAnalysisBosRecommendRequestMessageJsonImplicits.needAnaBRRequestMessageReads
    implicit val nasrReqReader: Reads[NeedAnalysisSenateRecommendRequestMessage] = NeedAnalysisSenateRecommendRequestMessageJsonImplicits.needAnaSRRequestMessageReads
    implicit val crvReqReader: Reads[CurriculumReviewRequestMessage] = CurriculumReviewRequestMessageJsonImplicits.crvRequestMessageReads
    implicit val fuReqReader: Reads[FindUserRequestMessage] = FindUserRequestMessageJsonImplicits.fuRequestMessageReads
    implicit val cuReqReader: Reads[CreateUserRequestMessage] = CreateUserRequestMessageJsonImplicits.cuRequestMessageReads
    implicit val cdaReqReader: Reads[CurriculumDevelopmentAuthorizationRequestMessage] = CurriculumDevelopmentAuthorizationRequestMessageJsonImplicits.cdaRequestMessageReads
    implicit val cmtMemReqReader: Reads[CommitteeMembersRequestMessage] = CommitteeMembersJsonImplicitsRequestMessageJsonImplicits.cmtMembersRequestMessageReads
    implicit val paCmtMemReqReader: Reads[CurriculumDevelopmentAppointPACRequestMessage] = CurriculumDevelopmentAppointPACRequestMessageJsonImplicits.cdpacmembRequestMessageReads
    implicit val cdCmtMemReqReader: Reads[CurriculumDevelopmentAppointCDCRequestMessage] = CurriculumDevelopmentAppointCDCRequestMessageJsonImplicits.cdcmembRequestMessageReads
    implicit val draftRevReqReader: Reads[CurriculumDevelopmentDraftRevisionRequestMessage] = CurriculumDevelopmentDraftRevisionRequestMessageJsonImplicits.cdDraftRevRequestMessageReads
    implicit val draftSubReqReader: Reads[CurriculumDevelopmentDraftSubmissionRequestMessage] = CurriculumDevelopmentDraftSubmissionRequestMessageJsonImplicits.cdDraftSubRequestMessageReads
    implicit val draftValReqReader: Reads[CurriculumDevelopmentDraftValidationRequestMessage] = CurriculumDevelopmentDraftValidationRequestMessageJsonImplicits.cdDraftValRequestMessageReads
    implicit val consReqReader: Reads[ConsultationRequestMessage] = ConsultationRequestMessageJsonImplicits.consRequestMessageReads
    implicit val bchReqReader: Reads[BenchmarkRequestMessage] = BenchmarkRequestMessageJsonImplicits.benchmarkRequestMessageReads
    implicit val fDraftReqReader: Reads[FinalDraftRequestMessage] = FinalDraftRequestMessageJsonImplicits.fdRequestMessageReads
    implicit val endReqReader: Reads[EndorsementRequestMessage] = EndorsementRequestMessageJsonImplicits.endRequestMessageReads

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

        recordTopic match {
            case "find-users-req" => {
                val smpMsg = Json.parse(recordValue).as[SimpleRequestMessage]
                val findUserMessage = new FindUserRequestMessage(smpMsg)
                actorMap("user") ! findUserMessage
            }
            case "create-users-req" => {
                val smpMsg1 = Json.parse(recordValue).as[SimpleRequestMessage]
                val createUserMessage = new CreateUserRequestMessage(smpMsg1)
                actorMap("user") ! createUserMessage
            }
            case "summary-req" => {
                val summaryMessage = Json.parse(recordValue).as[SimpleRequestMessage]
                actorMap("summary") ! summaryMessage
            }
            case "need-analysis-start-req" => {
                val needAnalysisStartMessage = Json.parse(recordValue).as[ProgrammeRequestMessage]
                actorMap("need-analysis") ! needAnalysisStartMessage
            }
            case "need-analysis-consult-req" => {
                println("spotted need analysis consultation case....")
                val needAnalysisConsultMessage = Json.parse(recordValue).as[NeedAnalysisConsultationRequestMessage]
                actorMap("need-analysis") ! needAnalysisConsultMessage
            }
            case "need-analysis-survey-req" => {
                val needAnalysisSurveyMessage = Json.parse(recordValue).as[NeedAnalysisSurveyRequestMessage]
                actorMap("need-analysis") ! needAnalysisSurveyMessage
            }
            case "need-analysis-conclude-req" => {
                println("handling need-analysis-conclude-req...")
                val needAnalysisConcludeMessage = Json.parse(recordValue).as[NeedAnalysisConcludeRequestMessage]
                actorMap("need-analysis") ! needAnalysisConcludeMessage
            }
            case "need-analysis-bos-start-req" => {
                val needAnalysisBSMessage = Json.parse(recordValue).as[NeedAnalysisBosStartRequestMessage]
                actorMap("need-analysis") ! needAnalysisBSMessage
            }
            case "need-analysis-bos-recommend-req" => {
                val needAnalysisBRMessage = Json.parse(recordValue).as[NeedAnalysisBosRecommendRequestMessage]
                actorMap("need-analysis") ! needAnalysisBRMessage
            }
            case "need-analysis-senate-start-req" => {
                val needAnalysisSSMessage = Json.parse(recordValue).as[NeedAnalysisSenateStartRequestMessage]
                actorMap("need-analysis") ! needAnalysisSSMessage
            }
            case "need-analysis-senate-recommend-req" => {
                val needAnalysisSRMessage = Json.parse(recordValue).as[NeedAnalysisSenateRecommendRequestMessage]
                actorMap("need-analysis") ! needAnalysisSRMessage
            }
            case "cur-dev-amendment-from-senate-req" => {
                val amendmentFromSenateReqMsg = Json.parse(recordValue).as[CurriculumDevelopmentAuthorizationRequestMessage]
                actorMap("curriculum-development") ! amendmentFromSenateReqMsg
            }
            case "cur-dev-authorize-from-senate-req" => {
                val authorizeFromSenateReqMsg = Json.parse(recordValue).as[CurriculumDevelopmentAuthorizationRequestMessage]
                actorMap("curriculum-development") ! authorizeFromSenateReqMsg
            }
            case "cur-dev-appoint-cdc-req" => {
                val cdcMembersReqMsg = Json.parse(recordValue).as[CurriculumDevelopmentAppointCDCRequestMessage]
                actorMap("curriculum-development") ! cdcMembersReqMsg
            }
            case "cur-dev-appoint-pac-req" => {
                val pacMembersReqMsg = Json.parse(recordValue).as[CurriculumDevelopmentAppointPACRequestMessage]
                actorMap("curriculum-development") ! pacMembersReqMsg
            }
            case "cur-dev-draft-revise-req" => {
                val draftRevisionReqMsg = Json.parse(recordValue).as[CurriculumDevelopmentDraftRevisionRequestMessage]
                actorMap("curriculum-development") ! draftRevisionReqMsg
            }
            case "cur-dev-draft-submit-req" => {
                val draftSubmissionReqMsg = Json.parse(recordValue).as[CurriculumDevelopmentDraftSubmissionRequestMessage]
                actorMap("curriculum-development") ! draftSubmissionReqMsg
            }
            case "cur-dev-draft-validate-req" => {
                val draftValidationReqMsg = Json.parse(recordValue).as[CurriculumDevelopmentDraftValidationRequestMessage]
                actorMap("curriculum-development") ! draftValidationReqMsg
            }
            case "consult-start-pac-req" => {
                val consultReqMsg = Json.parse(recordValue).as[ConsultationRequestMessage]
                actorMap("consultation") ! consultReqMsg
            }
            case "consult-benchmark-req" => {
                val benchmarkReqMsg = Json.parse(recordValue).as[BenchmarkRequestMessage]
                actorMap("consultation") ! benchmarkReqMsg
            }
            case "curriculum-review-req" => {
                val curriculumReviewMessage = Json.parse(recordValue).as[CurriculumReviewRequestMessage]
                actorMap("curriculum-development") ! curriculumReviewMessage
            }
            case "cur-dev-submit-to-bos-req" => {
                val submitToBosReqMsg = Json.parse(recordValue).as[CurriculumDevelopmentAuthorizationRequestMessage]
                actorMap("curriculum-development") ! submitToBosReqMsg
            }
            case "cur-dev-amendment-from-bos-req" => {
                val amendmentFromBosReqMsg = Json.parse(recordValue).as[CurriculumDevelopmentAuthorizationRequestMessage]
                actorMap("curriculum-development") ! amendmentFromBosReqMsg
            }
            case "cur-dev-authorize-from-bos-req" => {
                val authorizeFromBosReqMsg = Json.parse(recordValue).as[CurriculumDevelopmentAuthorizationRequestMessage]
                actorMap("curriculum-development") ! authorizeFromBosReqMsg
            }
            case "cur-dev-submit-to-senate-req" => {
                val submitToSenateReqMsg = Json.parse(recordValue).as[CurriculumDevelopmentAuthorizationRequestMessage]
                actorMap("curriculum-development") ! submitToSenateReqMsg
            }
            case "consult-final-draft-req" => {
                val fdReqMsg = Json.parse(recordValue).as[FinalDraftRequestMessage]
                actorMap("consultation") ! fdReqMsg
            }
            case "consult-endorse-req" => {
                val endReqMsg = Json.parse(recordValue).as[EndorsementRequestMessage]
                actorMap("consultation") ! endReqMsg
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
