package yester.lib

import play.api.libs.json.Json

final case class SinglePACCommitteeMember(firstName: String, lastName: String, organization: String, emailAddress: String, cellphone: String, workNumber: String)

object SinglePACCommitteeMemberJsonImplicits {
    implicit val singlePACComFmt = Json.format[SinglePACCommitteeMember]
    implicit val singlePACComWrites = Json.writes[SinglePACCommitteeMember]
    implicit val singlePACComReads = Json.reads[SinglePACCommitteeMember]
}
