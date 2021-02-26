package yester.message.request

import com.couchbase.client.scala.implicits.Codec
// import play.api.libs.json.{Json, Format}

final case class CreateUserRequestMessage (simpleMsg: SimpleRequestMessage){}

object CreateUserRequestMessage {
	implicit val codec: Codec[CreateUserRequestMessage] = Codec.codec[CreateUserRequestMessage]
}

// object CreateUserRequestMessageJsonImplicits {
//     implicit val simpleReqMsgFormat: Format[SimpleRequestMessage] =  SimpleRequestMessageJsonImplicits.simpleRequestMessageFmt

//     implicit val cuRequestMessageFmt = Json.format[CreateUserRequestMessage]
//     implicit val cuRequestMessageWrites = Json.writes[CreateUserRequestMessage]
//     implicit val cuRequestMessageReads = Json.reads[CreateUserRequestMessage]
// }
