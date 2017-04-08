package yester.util

// import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.reactivecouchbase.scaladsl.{N1qlQuery, ReactiveCouchbase, WriteSettings}
import scala.concurrent.Future
import play.api.libs.json.{Json, Format, Writes, Reads}
import yester.lib.{User, UserJsonImplicits, Programme, ProgrammeJsonImplicits, NeedAnalysisConsultation, NeedAnalysisConsultationJsonImplicits, NeedAnalysisSurvey, NeedAnalysisSurveyJsonImplicits}

object DBManager {
    val settings: WriteSettings = WriteSettings()
    val system  = ActorSystem("DBManagerCouchBase")
    implicit val materializer = ActorMaterializer.create(system)
    implicit val ec = system.dispatcher
    implicit val userFormat: Format[User] = UserJsonImplicits.userFmt
    implicit val userReader: Reads[User] = UserJsonImplicits.userReads
    implicit val progFormat: Format[Programme] = ProgrammeJsonImplicits.prgFmt
    implicit val progWriter: Writes[Programme] = ProgrammeJsonImplicits.prgWrites
    implicit val progReader: Reads[Programme] = ProgrammeJsonImplicits.prgReads
    implicit val naConsFormat: Format[NeedAnalysisConsultation] = NeedAnalysisConsultationJsonImplicits.needAnaConsFmt
    implicit val naConsWriter: Writes[NeedAnalysisConsultation] = NeedAnalysisConsultationJsonImplicits.needAnaConsWrites
    implicit val naSurvFormat: Format[NeedAnalysisSurvey] = NeedAnalysisSurveyJsonImplicits.needAnaSurvFmt
    implicit val naSurvWriter: Writes[NeedAnalysisSurvey] = NeedAnalysisSurveyJsonImplicits.needAnaSurvWrites

    val driver = ReactiveCouchbase(ConfigFactory.parseString(
        """
        buckets {
            bkt1 {
                name = "yester-users"
                hosts = ["10.100.253.150"]
            }
            bkt2 {
                name = "yester-programmes"
                hosts = ["10.100.253.150"]
            }
            bkt3 {
                name = "yester-consultations"
                hosts = ["10.100.253.150"]
            }
            bkt4 {
                name = "yester-na-surveys"
                hosts = ["10.100.253.150"]
            }
        }
        """.stripMargin), system)

    def save[T](bucketName: String, docKey: String, data: T, objFormat: Format[T]): Future[T] = {
        val curBucket = driver.bucket(bucketName)
        curBucket.insert(docKey, data, settings, objFormat)
    }

    def findById[T](bucketName: String, docKey: String, objReader: Reads[T]): Future[Option[T]] = {
        val curBucket = driver.bucket(bucketName)
        curBucket.get[T](docKey, objReader)
    }

    def findAll[T](bucketName: String, objReader: Reads[T]): Future[Seq[T]] = {
        val curBucket = driver.bucket(bucketName)
        val query = s"select * from $bucketName"
        curBucket.search(N1qlQuery(query), objReader).asSeq
    }

    def findUser(username: String): Future[Option[User]] = findById[User]("yester-users", username, userReader)

    def findAllProgrammes(): Future[List[Programme]] = findAll[Programme]("yester-programmes", progReader)

    def createProgramme(progKey: String, progData: Programme): Future[Programme] = save[Programme]("yester-programmes", progKey, progData, progFormat)
    def addNeedAnalysisConsultation(consulationKey: String, consultationData: NeedAnalysisConsultation): Future[NeedAnalysisConsultation] = save[NeedAnalysisConsultation]("yester-consultations", consulationKey, consultationData, naConsFormat)
    def addNeedAnalysisSurvey(surveyKey: String, surveyObj: NeedAnalysisSurvey): Future[NeedAnalysisSurvey] = save[NeedAnalysisSurvey]("yester-na-surveys", surveyKey, surveyObj, naSurvFormat)
}
