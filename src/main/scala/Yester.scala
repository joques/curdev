import akka.actor._
import scala.concurrent.ExecutionContext.Implicits.global

object Yester {
    def main(args: Array[String]) {
        println("welcome to Yester... The resource management micro service...")
        val topicList = List("find-users-req","create-users-req", "summary-req", "need-analysis-start-req")

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
