package config
import com.typesafe.config._

object AppConfig {
  private val config = ConfigFactory.load()

  object Database {
    val url: String = config.getString("database.url")
    val user: String = config.getString("database.user")
    val password: String = config.getString("database.password")
  }
  
  object Kafka {
    val bootrapServer: String = config.getString("kafka.bootstrap-server")
    val zookeeperConnect: String = config.getString("kafka.zookeeper-connect")
  }
}