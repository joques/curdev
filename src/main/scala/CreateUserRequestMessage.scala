import play.api.libs.json.{Json, Format}

case class CreateUserRequestMessage (simpleMsg: SimpleRequestMessage){}

object CreateUserRequestMessageJsonImplicits {
    implicit val simpleReqMsgFormat: Format[SimpleRequestMessage] =  SimpleRequestMessageJsonImplicits.simpleRequestMessageFmt

    implicit val cuRequestMessageFmt = Json.format[CreateUserRequestMessage]
    implicit val cuRequestMessageWrites = Json.writes[CreateUserRequestMessage]
    implicit val cuRequestMessageReads = Json.reads[CreateUserRequestMessage]
}
