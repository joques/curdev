import scala.concurrent.ExecutionContext.Implicits.global
import org.reactivecouchbase.ReactiveCouchbaseDriver

import scala.concurrent.Future
import play.api.libs.json._

object DBManager {
  val driver = ReactiveCouchbaseDriver()
  val usersBucket = driver.bucket("yester-users")
}
