/* 
========================================== 
DB Manager -- provides access to couchbase
========================================== 
*/

package yester.util

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import scala.concurrent.ExecutionContext.Implicits.global
import org.reactivecouchbase.rs.scaladsl.{ReactiveCouchbase, ViewQuery}
import scala.concurrent.Future
import com.couchbase.client.java.view.Stale

import org.reactivecouchbase.rs.scaladsl.json._

import play.api.libs.json.{Json, Format, Writes}

import yester.lib.{User, UserJsonImplicits, Programme, ProgrammeJsonImplicits, NeedAnalysis, NeedAnalysisJsonImplicits, CurriculumDevelopment, CurriculumDevelopmentJsonImplicits}

object DBManager {
	implicit val yesSys = ActorSystem("YesterReactiveCouchbaseSystem")
    implicit val materializer = ActorMaterializer.create(yesSys)
	implicit val ec = yesSys.dispatcher
	val driver = ReactiveCouchbase(ConfigFactory.load())
	
	// formatters and writers
	
	implicit val userFormat: JsonFormat[User] = UserJsonImplicits.userJsonFormat
	
	implicit val progFormat2: JsonFormat[Programme] = ProgrammeJsonImplicits.progJsonFormat

	implicit val naFormat2: JsonFormat[NeedAnalysis] = NeedAnalysisJsonImplicits.naJsonFormat

	implicit val cdFormat2: JsonFormat[CurriculumDevelopment] = CurriculumDevelopmentJsonImplicits.cdJsonFormat

	// data manipulation
	
  	def findUser(username: String): Future[Option[User]] = findById[User]("yester-users", username)
  	def findNeedAnalysisObject(naCode: String): Future[Option[NeedAnalysis]] = findById[NeedAnalysis]("yester-need-analyses", naCode)
  	def findCurriculumDevelopmentObject(devCode: String): Future[Option[CurriculumDevelopment]] = findById[CurriculumDevelopment]("yester-curricula-dev", devCode)

  	def findAllProgrammes(): Future[Seq[Programme]] = findAll[Programme]("yester-programmes", "progr_dd", "prog")

  	def createProgramme(progKey: String, progData: Programme): Future[Programme] = save[Programme]("yester-programmes", progKey, progData)
  	def addOrUpdateNeedAnalysis(key: String, naData: NeedAnalysis): Future[NeedAnalysis] = save[NeedAnalysis]("yester-need-analyses", key, naData)
  	def upsertCurriculumDevelopment(key: String, cdData: CurriculumDevelopment): Future[CurriculumDevelopment] = save[CurriculumDevelopment]("yester-curricula-dev", key, cdData)
	
	// generic methods

  	def findById[T](bucketName: String, docKey: String)(implicit valFormat: JsonFormat[T]): Future[Option[T]] = {
      val curBucket = driver.bucket(bucketName)
      curBucket.get[T](docKey)
  	}

  	def findAll[T](bucketName: String, designDoc: String, viewName: String)(implicit valFormat: JsonFormat[T]): Future[Seq[T]] = {
      val curBucket = driver.bucket(bucketName)
	  curBucket.searchView[T](ViewQuery(designDoc, viewName, _.includeDocs().stale(Stale.FALSE))).asSeq(materializer)
  	}

  	def save[T](bucketName: String, key: String, data: T)(implicit valFormat: JsonFormat[T]): Future[T] = {
      val curBucket = driver.bucket(bucketName)
      curBucket.upsert(key, data)
  	}
	
}
