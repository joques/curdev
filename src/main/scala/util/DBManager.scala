/*
 ==================================================================================================
 DB Manager -- provides access to CouchBase
 implements Couchase Scala SDK 1.1.2
 ==================================================================================================
 */


package yester.util

import com.couchbase.client.core.error.{CouchbaseException, DocumentNotFoundException}
import com.couchbase.client.scala.Cluster
import com.couchbase.client.scala.durability.Durability
import com.couchbase.client.scala.json._
import com.couchbase.client.scala.kv.{MutationResult, GetResult}
import com.couchbase.client.scala.query.QueryResult
import com.couchbase.client.scala.codec.{JsonSerializer, JsonDeserializer}

import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}
import concurrent.duration._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import yester.lib.{User, Programme, NeedAnalysis, CurriculumDevelopment}

object DBManager {
	val cluster = Cluster.connect("172.28.253.79", "curi", "curidev").get

	//find document inside a bucket using the document identifier
	def findDocByID(bucketName: String, docID: String): Future[GetResult] = {
		val curBucket = cluster.bucket(bucketName)
		curBucket.waitUntilReady(30.seconds).get

		val DocColl = curBucket.getDefaultCollection
		docColl.async.get(docID)
	}


	//find a user
	def findUser(username: String): Future[User] = {
		findDocByID("yester-users", username)
			.map((v: GetResult) => v.contentAs[User])
			.map((v: Try[User]) => v.get)
			.map((v: User) => v)
	}

	//find a need analysis document based on its code
	def findNeedAnalysisObject(naCode: String): Future[NeedAnaysis] = {
		findDocByID("yester-need-analyses", naCode)
			.map((v: GetResult) => v.contentAs[NeedAnalysis])
			.map((v: Try[NeedAnalysis]) => v.get)
			.map((v: NeedAnalysis) => v)
	}

	//find a curriculum development document
	def findCurriculumDevelopmentObject(devCode: String): Future[CurriculumDevelopment] = {
		findDocByID("yester-need-curricula-dev", devCode)
			.map((v: GetResult) => v.contentAs[CurriculumDevelopment])
			.map((v: Try[CurriculumDevelopment]) => v.get)
			.map((v: CurriculumDevelopment) => v)
	}

	//save a document in a bucket
	def saveDoc[T](bucketName: String, docID: String, docData: T)(implicit jser: JsonSerializer[T]): Future[MutationResult] = {
		val curBucket = cluster.bucket(bucketName)
		curBucket.waitUntilReady(30.seconds).get
		val docColl = curBucket.getDefaultCollection
		docColl.async.upsert(docID, docData)
	}

	//create a programme
	def createProgramme(progKey: String, progData: Programme): Future[MutaationResult] = {
		saveDoc[Programme]("yester-programmes",progKey, progData)
	}

	//add or update a need analysis document
	def addOrUpdateNeedAnalysis(naCode: String, naData: NeedAnalysis): Future[MutationResult] = {
		saveDoc[NeedAnalysis]("yester-need-analyses", naCode, naData)
	}

	//insert or update a curriculum development document
	def upsertCurriculumDevelopment(cdCode: String, cdData: CurriculumDevelopment): Future[MutationResult] = {
		saveDoc[CurriculumDevelopment]("yester-curricula-dev", cdCode, cdData)
	}

	//find all documents in a bucket
	def findAllDocs(statement: String): Future[QueryResult] = {
		cluster.async.query(statement)
	}

	def findAllProgrammes(): Future[QueryResult] = {
		val stmt = """SELECT `yester-programmes`.* FROM `yester-programmes`;"""
		findAllDocs(stmt)
	}
}
