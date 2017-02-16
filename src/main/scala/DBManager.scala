import scala.concurrent.ExecutionContext.Implicits.global
import org.reactivecouchbase.ReactiveCouchbaseDriver
import scala.concurrent.Future
import com.couchbase.client.protocol.views.{Stale, Query}
import play.api.libs.json.{Json, Format}
import net.spy.memcached.ops.OperationStatus

object DBManager {
  val driver = ReactiveCouchbaseDriver()
  implicit val userFormat: Format[User] = UserJsonImplicits.userFmt
  implicit val progFormat: Format[Programme] = ProgrammeJsonImplicits.prgFmt

  def findUser(username: String): Future[Option[User]] = findById[User](username, "yester-users")

  def findAllProgrammes(): Future[List[Programme]] = findAll[Programme]("yester-programmes", "progr_dd", "prog")

  def createProgramme(progData: Programme): Future[OperationStatus] = save[Programme]("yester-programmes", progData)

  def findById[T](docKey: String, bucketName: String)(implicit valFormat: Format[T]): Future[Option[T]] = {
      val curBucket = driver.bucket(bucketName)
      curBucket.get[T](docKey)
  }

  def findAll[T](bucketName: String, designDoc: String, viewName: String)(implicit valFormat: Format[T]): Future[List[T]] = {
      val curBucket = driver.bucket(bucketName)
      curBucket.find[T](designDoc, viewName)(new Query().setIncludeDocs(true).setStale(Stale.FALSE))
  }

  def save[T](bucketName: String, data: T)(implicit valFormat: Format[T]): Future[OperationStatus] = {
      val curBucket = driver.bucket(bucketName)
      curBucket.set[T](data)
  }
}
