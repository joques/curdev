import org.reactivecouchbase.ReactiveCouchbaseDriver
import scala.concurrent.Future
import play.api.libs.json._

object DBManager {
  val driver = ReactiveCouchbaseDriver()
  implicit val userFormat: Format[User] = UserJsonImplicits.userFmt

  def findUser(username: String): Future[Option[User]] = {
      val userBucket = driver.bucket("yester-users")
      userBucket.get[User](username)
  }
}
