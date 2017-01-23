import scala.concurrent.ExecutionContext.Implicits.global
import org.reactivecouchbase.ReactiveCouchbaseDriver
import scala.concurrent.Future
import play.api.libs.json._

object DBManager {
  val driver = ReactiveCouchbaseDriver()
  implicit val userFormat: Format[User] = UserJsonImplicits.userFmt

  def findId[T](key: String, bucketName: String): Future[Option[T]] = {
      val bucket = driver.bucket(bucketName)
      bucket.get[T](key)
  }

  def findUser(username: String): Future[Option[User]] = findId[User](username, "yester-users")

  // def findUser(username: String): Future[Option[User]] = {
  //     val userBucket = driver.bucket("yester-users")
  //     userBucket.get[User](username)
  // }

  def findAllProgrammes(): Future[List[Programme]] = {
      val programmeBucket = driver.bucket("yester-programmes")
      programmeBucket.find[Programme]
  }
}
