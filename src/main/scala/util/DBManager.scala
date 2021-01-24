import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.reactivecouchbase.rs.scaladsl.{N1qlQuery, ReactiveCouchbase}
import org.reactivecouchbase.rs.scaladsl.json._
import play.api.libs.json._
import akka.stream.scaladsl.Sink
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import scala.concurrent.Future
import play.api.libs.json.{Json, Format, Writes}
import net.spy.memcached.{PersistTo, ReplicateTo}
import yester.lib.{User, UserJsonImplicits, Programme, ProgrammeJsonImplicits, NeedAnalysis, NeedAnalysisJsonImplicits, CurriculumDevelopment, CurriculumDevelopmentJsonImplicits}



import scala.concurrent.ExecutionContext.Implicits.global
import org.reactivecouchbase.ReactiveCouchbaseDriver


import org.reactivecouchbase.client.{OpResult, Constants}
import com.couchbase.client.protocol.views.{Stale, Query}


object DBManager {
	val system = ActorSystem("YesterReactiveCouchbaseSystem")
	implicit val materialiser = ActorMaterializer(system)
	implicit val ec = system.dispatcher
	
	val driver = ReactiveCouchbase(ConfigFactory.load())
}
