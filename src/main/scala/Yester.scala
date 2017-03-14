package yester

import akka.actor._
import scala.concurrent.ExecutionContext.Implicits.global
import yester.message.processor.{NeedAnalysisMessageProcessor, CurriculumDevelopmentMessageProcessor, SummaryMessageProcessor, UsertMessageProcessor}

object Yester {
    def main(args: Array[String]) {
        println("welcome to Yester... The resource management micro service...")
        val topicList = List("find-users-req","create-users-req", "summary-req", "need-analysis-start-req", "need-analysis-conclude-req", "need-analysis-consult-req")

        val yConsumer = new YesterConsumer(topicList)
        val yProducer = new YesterProducer()

        val actorSystem = ActorSystem("yester")
        val naMsgProc = actorSystem.actorOf(Props(new NeedAnalysisMessageProcessor(yProducer)), "need-analysis")
        val cdMsgProc = actorSystem.actorOf(Props(new CurriculumDevelopmentMessageProcessor(yProducer)), "curriculum-development")
        val sumMsgProc = actorSystem.actorOf(Props(new SummaryMessageProcessor(yProducer)), "summary")
        val userMsgProc = actorSystem.actorOf(Props(new UsertMessageProcessor(yProducer)), "user")

        val actorMap = Map("need-analysis" -> naMsgProc, "curriculum-development" -> cdMsgProc, "summary" -> sumMsgProc, "user" -> userMsgProc)

        println("displaying consumer and producer...")
        println(yConsumer.toString)
        println("calling read() from yester...")

        yConsumer.startConsuming(actorMap)
    }
}
