import play.api.libs.json.Json

case class CreateUserRequestMessage (simpleMsg: SimpleRequestMessage){}

object CreateUserRequestMessageJsonImplicits {
    implicit val simpleReqMsgFormat: Format[SimpleRequestMessage] =  SimpleRequestMessageJsonImplicits.simpleRequestMessageFmt

    implicit val cuRequestMessageFmt = Json.format[FindUserRequestMessage]
    implicit val cuRequestMessageWrites = Json.writes[FindUserRequestMessage]
    implicit val cuRequestMessageReads = Json.reads[FindUserRequestMessage]
}
