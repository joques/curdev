import scala.concurrent.ExecutionContext.Implicits.global
import org.reactivecouchbase.ReactiveCouchbaseDriver

case class DBManager {
  val driver = ReactiveCouchbaseDriver()
  val usersBucket = driver.bucket("yester-users")
}
