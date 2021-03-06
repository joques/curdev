package yester.message.processor

import akka.actor._
import org.apache.kafka.clients.producer.ProducerRecord
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{Reads, Json, Writes}
import java.util.UUID
import yester.YesterProducer
import yester.util.DBManager
import yester.lib.{Programme, UserWithPreProgramme}
import yester.message.request.{FindUserRequestMessage, CreateUserRequestMessage}
import yester.message.response.UserResponseMessage._
import yester.message.response.UserWithPreProgrammeResponseMessage._
import yester.message.response.{UserResponseMessage, UserWithPreProgrammeResponseMessage}


final case class UserMessageProcessor(messenger: YesterProducer) extends MessageProcessor(messenger) {

    // implicit val userRespWriter: Writes[UserResponseMessage] = UserResponseMessageJsonImplicits.userResponseMessageWrites
    // implicit val userWPRespWriter: Writes[UserWithPreProgrammeResponseMessage] = UserWithPreProgrammeResponseMessageJsonImplicits.uwPPResponseMessageWrites


    def receive = {
        case fUserReqMsg: FindUserRequestMessage =>
            println("received find-users-req message ...")
            findUserWithPreProgramme(fUserReqMsg)
        case cUserReqMsg: CreateUserRequestMessage =>
            println("received create-users-req message ...")
            createUser(cUserReqMsg)
        case _ =>
            println("unknown message type ...")
    }

    // will be deleted
    def findUser(message: FindUserRequestMessage): Unit = {
        val userName = message.simpleMsg.content
        println(s"finding user $userName")
        val userResult = DBManager.findUser(userName)
        userResult.onComplete {
            case Success(userVal) => {
                println(s"We got user $userVal")
                val userSuccessRespMsg: UserResponseMessage = new UserResponseMessage(message.simpleMsg.messageId, None, Some(userVal))
                val succMsgStr = Json.toJson(userSuccessRespMsg).toString()
                println(s"the success message to be sent is $succMsgStr")
                messenger.getProducer().send(new ProducerRecord[String,String]("find-users-res", succMsgStr))
            }
            case Failure(userErr) => {
                userErr.printStackTrace
                val userErrorRespMsg: UserResponseMessage = new UserResponseMessage(message.simpleMsg.messageId, Option(userErr.getMessage), None)
                val errMsgStr = Json.toJson(userErrorRespMsg).toString()
                println(s"the error message to be sent it $errMsgStr")
                messenger.getProducer().send(new ProducerRecord[String,String]("find-users-res", errMsgStr))
            }
        }
    }


    def findUserWithPreProgramme(message: FindUserRequestMessage): Unit = {
        val userName = message.simpleMsg.content
        println(s"finding user $userName")
        val userResult = DBManager.findUser(userName)
        userResult.onComplete {
            case Success(userVal) => {
                println(s"We got user $userVal")

                println("Will now look for pre programmes")

                val allProgs = DBManager.findAllProgrammes()

                allProgs.onComplete {
                    case Failure(progError) => {
                        val progListErrorRespMsg: UserWithPreProgrammeResponseMessage = new UserWithPreProgrammeResponseMessage(message.simpleMsg.messageId, Option(progError.getMessage), None)
                        val errMsgStr1 = Json.toJson(progListErrorRespMsg).toString()
                        println(s"the error message to be sent is $errMsgStr1")
                        messenger.getProducer().send(new ProducerRecord[String,String]("find-users-res", errMsgStr1))
                    }
                    case Success(progs) => {
                        progs.rowsAs[Programme] match {
                            case Failure(rowError) => {
                                val rowErrorRespMsg: UserWithPreProgrammeResponseMessage = new UserWithPreProgrammeResponseMessage(message.simpleMsg.messageId, Option(rowError.getMessage), None)
                                val errMsgStr11 = Json.toJson(rowErrorRespMsg).toString()
                                println(s"the error message to be sent is $errMsgStr11")
                                messenger.getProducer().send(new ProducerRecord[String,String]("find-users-res", errMsgStr11))
                            }
                            case Success(progSeq) => {
                                val preProgrammeSeq: Seq[Programme] = progSeq.filter((prg: Programme) => prg.isPreProgramme)
                                var preProgCodes: Seq[String] = for (prg1 <- preProgrammeSeq if prg1.preProgComponent.get.initiator == userName) yield prg1.preProgComponent.get.devCode

                                var userWPrePrg: Option[UserWithPreProgramme] = None
                                if (preProgCodes.isEmpty) {
                                    userWPrePrg = Some(new UserWithPreProgramme(userVal, None))
                                } else {
                                    userWPrePrg = Some(new UserWithPreProgramme(userVal, Option(preProgCodes)))
                                }
                                val succRespMsg: UserWithPreProgrammeResponseMessage = new UserWithPreProgrammeResponseMessage(message.simpleMsg.messageId, None, userWPrePrg)
                                val succMsgStr = Json.toJson(succRespMsg).toString()
                                println(s"the success message to be sent is $succMsgStr")
                                messenger.getProducer().send(new ProducerRecord[String,String]("find-users-res", succMsgStr))
                            }
                        }
                    }
                }
            }
            case Failure(userErr) => {
                userErr.printStackTrace
                val userErrorRespMsg: UserWithPreProgrammeResponseMessage = new UserWithPreProgrammeResponseMessage(message.simpleMsg.messageId, Option(userErr.getMessage), None)
                val errMsgStr = Json.toJson(userErrorRespMsg).toString()
                println(s"the error message to be sent it $errMsgStr")
                messenger.getProducer().send(new ProducerRecord[String,String]("find-users-res", errMsgStr))
            }
        }
    }

    def createUser(message: CreateUserRequestMessage): Unit = {
        println("creating new user...")
    }
}
