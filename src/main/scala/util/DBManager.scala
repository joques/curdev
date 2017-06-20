package yester.util

import scala.concurrent.ExecutionContext.Implicits.global
import org.reactivecouchbase.ReactiveCouchbaseDriver
import scala.concurrent.Future
import org.reactivecouchbase.client.{OpResult, Constants}
import com.couchbase.client.protocol.views.{Stale, Query}
import play.api.libs.json.{Json, Format, Writes}
import net.spy.memcached.{PersistTo, ReplicateTo}
import yester.lib.{User, UserJsonImplicits, Programme, ProgrammeJsonImplicits, NeedAnalysisConsultation, NeedAnalysisConsultationJsonImplicits, NeedAnalysisSurvey, NeedAnalysisSurveyJsonImplicits, NeedAnalysisConclude, NeedAnalysisConcludeJsonImplicits}

object DBManager {
  val driver = ReactiveCouchbaseDriver()
  implicit val userFormat: Format[User] = UserJsonImplicits.userFmt

  implicit val progFormat: Format[Programme] = ProgrammeJsonImplicits.prgFmt
  implicit val progWriter: Writes[Programme] = ProgrammeJsonImplicits.prgWrites

  implicit val naConsFormat: Format[NeedAnalysisConsultation] = NeedAnalysisConsultationJsonImplicits.needAnaConsFmt
  implicit val naConsWriter: Writes[NeedAnalysisConsultation] = NeedAnalysisConsultationJsonImplicits.needAnaConsWrites

  implicit val naSurvFormat: Format[NeedAnalysisSurvey] = NeedAnalysisSurveyJsonImplicits.needAnaSurvFmt
  implicit val naSurvWriter: Writes[NeedAnalysisSurvey] = NeedAnalysisSurveyJsonImplicits.needAnaSurvWrites

  implicit val naConclFormat: Format[NeedAnalysisConclude] = NeedAnalysisConcludeJsonImplicits.needAnaConclFmt
  implicit val naConclWriter: Writes[NeedAnalysisConclude] = NeedAnalysisConcludeJsonImplicits.needAnaConclWrites

  def findUser(username: String): Future[Option[User]] = findById[User]("yester-users", username)

  def findAllProgrammes(): Future[List[Programme]] = findAll[Programme]("yester-programmes", "progr_dd", "prog")

  def createProgramme(progKey: String, progData: Programme): Future[OpResult] = save[Programme]("yester-programmes", progKey, progData)

  def addNeedAnalysisConsultation(consulationKey: String, consultationData: NeedAnalysisConsultation): Future[OpResult] = save[NeedAnalysisConsultation]("yester-consultations", consulationKey, consultationData)

  def addNeedAnalysisSurvey(surveyKey: String, surveyObj: NeedAnalysisSurvey): Future[OpResult] = save[NeedAnalysisSurvey]("yester-na-surveys", surveyKey, surveyObj)

  def addNeedAnalysisConclusion(conclusionKey: String, conclusionObj: NeedAnalysisConclude): Future[OpResult] = save[NeedAnalysisConclude]("yester-na-conclusions", conclusionKey, conclusionObj)

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
