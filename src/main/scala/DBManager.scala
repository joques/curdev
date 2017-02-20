import scala.concurrent.ExecutionContext.Implicits.global
import org.reactivecouchbase.ReactiveCouchbaseDriver
import scala.concurrent.Future
import org.reactivecouchbase.client.{OpResult, Constants}
import com.couchbase.client.protocol.views.{Stale, Query}
import play.api.libs.json.{Json, Format, Writes}
import net.spy.memcached.{PersistTo, ReplicateTo}
// import net.spy.memcached.ops.OperationStatus

object DBManager {
  val driver = ReactiveCouchbaseDriver()
  implicit val userFormat: Format[User] = UserJsonImplicits.userFmt

  implicit val progFormat: Format[Programme] = ProgrammeJsonImplicits.prgFmt
  implicit val progWriter: Writes[Programme] = ProgrammeJsonImplicits.prgWrites

  implicit val naConsFormat: Format[NeedAnalysisConsultation] = NeedAnalysisConsultationJsonImplicits.needAnaConsFmt
  implicit val naConsWriter: Writes[NeedAnalysisConsultation] = NeedAnalysisConsultationJsonImplicits.needAnaConsWrites

  def findUser(username: String): Future[Option[User]] = findById[User]("yester-users", username)

  def findAllProgrammes(): Future[List[Programme]] = findAll[Programme]("yester-programmes", "progr_dd", "prog")

  def createProgramme(progKey: String, progData: Programme): Future[OpResult] = save[Programme]("yester-programmes", progKey, progData)

  def addNeedAnalysisConsultation(consulationKey: String, consultationData: NeedAnalysisConsultation): Future[OpResult] = save[NeedAnalysisConsultation]("yester-consultations", consulationKey, consultationData)



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
