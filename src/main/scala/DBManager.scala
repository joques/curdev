import com.rethinkscala.net.Version3
import com.rethinkscala.Connection
import com.rethinkscala.ast.DB

case class DBManager {
    lazy val version = new Version3("10.100.253.150", 28015)
    lazy implicit val connection = Blocking(version)
    lazy val db = DB("yesterdb")
}
