package yester.message.request

import com.couchbase.client.scala.implicits.Codec


// import play.api.libs.json.{Reads, Json, Format}

import yester.lib.DraftRevision

final case class CurriculumDevelopmentDraftRevisionRequestMessage(messageId: String, content: DraftRevision) extends ComplexRequestMessage[DraftRevision](messageId, content)

object CurriculumDevelopmentDraftRevisionRequestMessage {
	implicit val codec: Codec[CurriculumDevelopmentDraftRevisionRequestMessage] = Codec.codec[CurriculumDevelopmentDraftRevisionRequestMessage]
}

// object CurriculumDevelopmentDraftRevisionRequestMessageJsonImplicits {
//     implicit val draftRevFormat: Format[DraftRevision] =  DraftRevisionJsonImplicits.draftRevFmt

//     implicit val cdDraftRevRequestMessageFmt = Json.format[CurriculumDevelopmentDraftRevisionRequestMessage]
//     implicit val cdDraftRevRequestMessageeWrites = Json.writes[CurriculumDevelopmentDraftRevisionRequestMessage]
//     implicit val cdDraftRevRequestMessageReads = Json.reads[CurriculumDevelopmentDraftRevisionRequestMessage]
// }
