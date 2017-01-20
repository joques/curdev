import scala.concurrent.ExecutionContext.Implicits.global
import org.reactivecouchbase.ReactiveCouchbaseDriver

import scala.concurrent.Future
import play.api.libs.json._

case class User(username: String, password: String, profile: String, faculty: String, department: String)

object DBManager {
  val driver = ReactiveCouchbaseDriver()

  def findUser(username: String): Future[Option[User]] = {
      val userBucket = driver.bucket("yester-users")
      userBucket.get[User](username)
  }
}
