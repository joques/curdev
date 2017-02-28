import akka.actor._
import play.api.libs.json.{Reads, Json, Writes}
import java.util.UUID
import yester.util._

final case class NeedAnalysisMessageProcessor(messenger: YesterProducer) extends MessageProcessor(messenger) {
    def receive = {
        case prgReqMsg: ProgrammeRequestMessage =>
            println("received need-analysis-start-req message ...")
            createPreProgramme(prgReqMsg)
        case naConsReqMsg: NeedAnalysisConsultationRequestMessage =>
            println("received need-analysis-consult-req message ...")
            addNeedAnalysisConsultation(naConsReqMsg)
        case _ =>
            println("unknown message ...")
    }

    def createPreProgramme(message: ProgrammeRequestMessage): Unit = {
        println("creating a new programme object ...")

        val progObj = message.content
        val progKey = UUID.randomUUID().toString()
        val createProgOpRes = DBManager.createProgramme(progKey, progObj)

        handleInsertionResultWithSimpleResponse(createProgOpRes, message.messageId, "need-analysis-start-res")
    }

    def addNeedAnalysisConsultation(message: NeedAnalysisConsultationRequestMessage): Unit = {
        println("adding consultation record for need analysis")

        val consultationObj = message.content
        val consulationKey = UUID.randomUUID().toString()

        val addConsultationOpRes = DBManager.addNeedAnalysisConsultation(consulationKey, consultationObj)

        handleInsertionResultWithSimpleResponse(addConsultationOpRes, message.messageId, "need-analysis-consult-res")
    }
}
