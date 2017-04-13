package yester.util

import scala.concurrent.ExecutionContext.Implicits.global
import org.reactivecouchbase.ReactiveCouchbaseDriver
import scala.concurrent.Future
import org.reactivecouchbase.client.{OpResult, Constants}
import com.couchbase.client.protocol.views.{Stale, Query}
import play.api.libs.json.{Json, Format, Writes}
import net.spy.memcached.{PersistTo, ReplicateTo}
import yester.lib.{User, UserJsonImplicits, Programme, ProgrammeJsonImplicits, NeedAnalysisConsultation, NeedAnalysisConsultationJsonImplicits, NeedAnalysisSurvey, NeedAnalysisSurveyJsonImplicits}

object DBManager {
  val driver = ReactiveCouchbaseDriver()
  implicit val userFormat: Format[User] = UserJsonImplicits.userFmt
  implicit val progFormat: Format[Programme] = ProgrammeJsonImplicits.prgFmt
  implicit val progWriter: Writes[Programme] = ProgrammeJsonImplicits.prgWrites
  implicit val naConsFormat: Format[NeedAnalysisConsultation] = NeedAnalysisConsultationJsonImplicits.needAnaConsFmt
  implicit val naConsWriter: Writes[NeedAnalysisConsultation] = NeedAnalysisConsultationJsonImplicits.needAnaConsWrites
  implicit val naSurvFormat: Format[NeedAnalysisSurvey] = NeedAnalysisSurveyJsonImplicits.needAnaSurvFmt
  implicit val naSurvWriter: Writes[NeedAnalysisSurvey] = NeedAnalysisSurveyJsonImplicits.needAnaSurvWrites

  def findUser(username: String): Future[Option[User]] = findById[User]("yester-users", username)

  def findAllProgrammes(): Future[List[Programme]] = findAll[Programme]("yester-programmes", "progr_dd", "prog")

  def createProgramme(progKey: String, progData: Programme): Future[OpResult] = save[Programme]("yester-programmes", progKey, progData)

  def addNeedAnalysisConsultation(consulationKey: String, consultationData: NeedAnalysisConsultation): Future[OpResult] = save[NeedAnalysisConsultation]("yester-consultations", consulationKey, consultationData)

  def addNeedAnalysisSurvey(surveyKey: String, surveyObj: NeedAnalysisSurvey): Future[OpResult] = save[NeedAnalysisSurvey]("yester-na-surveys", surveyKey, surveyObj)



  def findById[T](bucketName: String, docKey: String)(implicit valFormat: Format[T]): Future[Option[T]] = {
      val curBucket = driver.bucket(bucketName)
      curBucket.get[T](docKey)
  }

  def findAll[T](bucketName: String, designDoc: String, viewName: String)(implicit valFormat: Format[T]): Future[List[T]] = {
      val curBucket = driver.bucket(bucketName)
      curBucket.find[T](designDoc, viewName)(new Query().setIncludeDocs(true).setStale(Stale.FALSE))
  }

  def save[T](bucketName: String, key: String, data: T)(implicit valFormat: Format[T]): Future[OpResult] = {
      val curBucket = driver.bucket(bucketName)
      curBucket.set(key, data, Constants.expiration, PersistTo.ZERO, ReplicateTo.ZERO)
  }
}

// import scala.concurrent.ExecutionContext.Implicits.global
// import akka.actor.ActorSystem
// import akka.stream.ActorMaterializer
// import com.typesafe.config.ConfigFactory
// import org.reactivecouchbase.scaladsl.{N1qlQuery, ReactiveCouchbase, WriteSettings}
// import scala.concurrent.Future
// import play.api.libs.json.{Json, Format, Writes, Reads}
// import yester.lib.{User, UserJsonImplicits, Programme, ProgrammeJsonImplicits, NeedAnalysisConsultation, NeedAnalysisConsultationJsonImplicits, NeedAnalysisSurvey, NeedAnalysisSurveyJsonImplicits}
//
// object DBManager {
//     val settings: WriteSettings = WriteSettings()
//     val system  = ActorSystem("DBManagerCouchBase")
//     implicit val materializer = ActorMaterializer.create(system)
//     implicit val ec = system.dispatcher
//     implicit val userFormat: Format[User] = UserJsonImplicits.userFmt
//     implicit val userReader: Reads[User] = UserJsonImplicits.userReads
//     implicit val progFormat: Format[Programme] = ProgrammeJsonImplicits.prgFmt
//     implicit val progWriter: Writes[Programme] = ProgrammeJsonImplicits.prgWrites
//     implicit val progReader: Reads[Programme] = ProgrammeJsonImplicits.prgReads
//     implicit val naConsFormat: Format[NeedAnalysisConsultation] = NeedAnalysisConsultationJsonImplicits.needAnaConsFmt
//     implicit val naConsWriter: Writes[NeedAnalysisConsultation] = NeedAnalysisConsultationJsonImplicits.needAnaConsWrites
//     implicit val naSurvFormat: Format[NeedAnalysisSurvey] = NeedAnalysisSurveyJsonImplicits.needAnaSurvFmt
//     implicit val naSurvWriter: Writes[NeedAnalysisSurvey] = NeedAnalysisSurveyJsonImplicits.needAnaSurvWrites
//
//     val driver = ReactiveCouchbase(ConfigFactory.parseString(
//         """
//             buckets {
//                 bkt1 {
//                     name = "yester-users"
//                     hosts = ["10.100.253.150"]
//                 }
//                 bkt2 {
//                     name = "yester-programmes"
//                     hosts = ["10.100.253.150"]
//                 }
//                 bkt3 {
//                     name = "yester-consultations"
//                     hosts = ["10.100.253.150"]
//                 }
//                 bkt4 {
//                     name = "yester-na-surveys"
//                     hosts = ["10.100.253.150"]
//                 }
//             }
//         """.stripMargin), system)
//
//     def save[T](bucketName: String, docKey: String, data: T, objFormat: Format[T]): Future[T] = {
//         val curBucket = driver.bucket(bucketName)
//         curBucket.insert(docKey, data, settings, objFormat)
//     }
//
//     def findById[T](bucketName: String, docKey: String, objReader: Reads[T]): Future[Option[T]] = {
//         val curBucket = driver.bucket(bucketName)
//         curBucket.get[T](docKey, objReader)
//     }
//
//     def findAll[T](bucketCode: String, bucketName: String, objReader: Reads[T]): Future[Seq[T]] = {
//         println(s"inside findAll with bucketName $bucketName")
//         val curBucket = driver.bucket(bucketCode)
//         println(s"showing the driver $curBucket")
//         val query = s"select * from $bucketName"
//         println(s"checking query :: $query")
//         curBucket.search(N1qlQuery(query), objReader).asSeq
//     }
//
//     def findUser(username: String): Future[Option[User]] = findById[User]("bkt1", username, userReader)
//
//     def findAllProgrammes(): Future[Seq[Programme]] = findAll[Programme]("bkt2", "yester-programmes", progReader)
//
//     def createProgramme(progKey: String, progData: Programme): Future[Programme] = save[Programme]("bkt2", progKey, progData, progFormat)
//     def addNeedAnalysisConsultation(consulationKey: String, consultationData: NeedAnalysisConsultation): Future[NeedAnalysisConsultation] = save[NeedAnalysisConsultation]("bkt3", consulationKey, consultationData, naConsFormat)
//     def addNeedAnalysisSurvey(surveyKey: String, surveyObj: NeedAnalysisSurvey): Future[NeedAnalysisSurvey] = save[NeedAnalysisSurvey]("bkt4", surveyKey, surveyObj, naSurvFormat)
// }
