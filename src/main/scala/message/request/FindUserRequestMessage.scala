package yester.message.request

import play.api.libs.json.{Json, Format}

final case class FindUserRequestMessage (simpleMsg: SimpleRequestMessage){}

object FindUserRequestMessageJsonImplicits {
    implicit val simpleReqMsgFormat: Format[SimpleRequestMessage] =  SimpleRequestMessageJsonImplicits.simpleRequestMessageFmt

    implicit val fuRequestMessageFmt = Json.format[FindUserRequestMessage]
    implicit val fuRequestMessageWrites = Json.writes[FindUserRequestMessage]
    implicit val fuRequestMessageReads = Json.reads[FindUserRequestMessage]
}
