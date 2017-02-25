import akka.actor._
import play.api.libs.json.{Reads, Json, Writes}
import java.util.UUID

case class NeedAnalysisMessageProcessor(messenger: YesterProducer) extends MessageProcessor(messenger) {
    def receive = {
        case "need-analysis-start-req" =>
            println("received need-analysis-start-req message")
            val needAnalysisStartMessage = Json.parse(recordValue).as[ProgrammeRequestMessage]
            createPreProgramme(needAnalysisStartMessage)
        case "need-analysis-consult-req" =>
            println("received need-analysis-consult-req message")
    }

    def createPreProgramme(message: ProgrammeRequestMessage): Unit = {
        println("creating a new programme object ...")

        val progObj = message.content
        val progKey = UUID.randomUUID().toString()
        val createProgOpRes = DBManager.createProgramme(progKey, progObj)

        handleInsertionResultWithSimpleResponse(createProgOpRes, message.messageId, "need-analysis-start-res")
    }
}
