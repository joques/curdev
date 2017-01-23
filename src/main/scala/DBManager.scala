import scala.concurrent.ExecutionContext.Implicits.global
import org.reactivecouchbase.ReactiveCouchbaseDriver
import scala.concurrent.Future
import play.api.libs.json.{Json, Format}

object DBManager {
  val driver = ReactiveCouchbaseDriver()
  implicit val userFormat: Format[User] = UserJsonImplicits.userFmt

  def findUser(username: String): Future[Option[User]] = findById[User](username, "yester-users")

  def findAllProgrammes(designDoc: String, viewName: String): Future[Option[List[Programme]]] = findAll[Programme]("yester-programmes", "progr_dd", "progr")

  def findById[T](docKey: String, bucketName: String)(implicit valFormat: Format[T]): Future[Option[T]] = {
      val curBucket = driver.bucket(bucketName)
      curBucket.get[T](docKey)
  }

  def findAll[T](bucketName: String, designDoc: String, viewName: String)(implicit valFormat: Format[T]): Future[Option[List[T]]] = {
      val curBucket = driver.bucket(bucketName)
      curBucket.find[T](designDoc, viewName)(new Query().setIncludeDocs(true).setStale(Stale.FALSE))
  }
}
