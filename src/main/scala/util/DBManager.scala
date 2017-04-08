package yester.util

import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.reactivecouchbase.scaladsl.{N1qlQuery, ReactiveCouchbase}
import scala.concurrent.Future
import play.api.libs.json.{Json, Format, Writes}
import yester.lib.{User, UserJsonImplicits, Programme, ProgrammeJsonImplicits, NeedAnalysisConsultation, NeedAnalysisConsultationJsonImplicits, NeedAnalysisSurvey, NeedAnalysisSurveyJsonImplicits}

object DBManager {
    val system  = ActorSystem("DBManagerCouchBase")
    implicit val materializer = ActorMaterializer(system)
    implicit val ec = system.dispatcher
    implicit val userFormat: Format[User] = UserJsonImplicits.userFmt
    implicit val progFormat: Format[Programme] = ProgrammeJsonImplicits.prgFmt
    implicit val progWriter: Writes[Programme] = ProgrammeJsonImplicits.prgWrites
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

    def save[T](bucketName: String, docKey: String, data: T): Future[T] = {
        val curBucket = driver.bucket(bucketName)
        curBucket.insert(docKey, data)
    }

    def findById[T](bucketName: String, docKey: String): Future[Option[T]] = {
        val curBucket = driver.bucket(bucketName)
        curBucket.get[T](docKey)
    }

    def findAll[T](bucketName: String): Future[List[T]] = {
        val curBucket = driver.bucket(bucketName)
        val query = s"select * from $bucketName"
        curBucket.search(N1qlQuery(query)).asSeq
    }

    def findUser(username: String): Future[Option[User]] = findById[User]("yester-users", username)

    def findAllProgrammes(): Future[List[Programme]] = findAll[Programme]("yester-programmes")

    def createProgramme(progKey: String, progData: Programme): Future[Programme] = save[Programme]("yester-programmes", progKey, progData)

    def addNeedAnalysisConsultation(consulationKey: String, consultationData: NeedAnalysisConsultation): Future[NeedAnalysisConsultation] = save[NeedAnalysisConsultation]("yester-consultations", consulationKey, consultationData)
    def addNeedAnalysisSurvey(surveyKey: String, surveyObj: NeedAnalysisSurvey): Future[NeedAnalysisSurvey] = save[NeedAnalysisSurvey]("yester-na-surveys", surveyKey, surveyObj)
}
