import akka.actor._

object Yester {
    def main(args: Array[String]) {
        println("welcome to Yester... The resource management micro service...")
        val topicList = List("find-users-req","create-users-req", "summary-req", "need-analysis-start-req")

        val yConsumer = new YesterConsumer(topicList)
        val yProducer = new YesterProducer()

        val actorSystem = ActorSystem("yester")
        val naMsgProc:NeedAnalysisMessageProcessor = actorSystem.actorOf(Props[NeedAnalysisMessageProcessor], "need-analysis")
        naMsgProc.addMessenger(yProducer)


        // val actorMap = Map()

        println("displaying consumer and producer...")
        println(yConsumer.toString)
        println("calling read() from yester...")

        yConsumer.startConsuming(yProducer)
    }
}
