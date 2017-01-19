import scala.concurrent.ExecutionContext.Implicits.global
import org.reactivecouchbase.ReactiveCouchbaseDriver

object DBManager {
  val driver = ReactiveCouchbaseDriver()
  val usersBucket = driver.bucket("yester-users")
}
