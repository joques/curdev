import scala.concurrent.ExecutionContext.Implicits.global
import org.reactivecouchbase.ReactiveCouchbaseDriver
import scala.concurrent.Future
import play.api.libs.json._

object DBManager {
  val driver = ReactiveCouchbaseDriver()
  implicit val userFormat: Format[User] = UserJsonImplicits.userFmt

  // def findUser(username: String): Future[Option[User]] = {
  //     val userBucket = driver.bucket("yester-users")
  //     userBucket.get[User](username)
  // }

  def findUser(username: String): Future[Option[User]] = findById[User](username, "yester-users")

  def findById[T](docKey: String, bucketName: String, implicit valFormat: Format[T]): Future[Option[T]] = {
      val curBucket = driver.bucket(bucketName)
      curBucket.get[T](docKey)
  }
}
