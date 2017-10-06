package yester.lib

import play.api.libs.json.Json

final case class SingleCommitteeMember(firstName: String, lastName: String, organization: String, emailAddress: String, cellphone: String, workNumber: String)

object SingleCommitteeMemberJsonImplicits {
    implicit val singleComFmt = Json.format[SingleCommitteeMember]
    implicit val singleComWrites = Json.writes[SingleCommitteeMember]
    implicit val singleComReads = Json.reads[SingleCommitteeMember]
}
