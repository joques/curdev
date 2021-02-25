package yester.lib

// import play.api.libs.json.Json

import com.couchbase.client.scala.implicits.Codec

final case class SingleCommitteeMember(firstName: String, lastName: String, organization: String, emailAddress: String, cellphone: String, workNumber: String)

object SingleCommitteeMember {
    implicit val sCMCodec: Codec[SingleCommitteeMember] = Codec.codec[SingleCommitteeMember]
}


// object SingleCommitteeMemberJsonImplicits {
//     implicit val singleComFmt = Json.format[SingleCommitteeMember]
//     implicit val singleComWrites = Json.writes[SingleCommitteeMember]
//     implicit val singleComReads = Json.reads[SingleCommitteeMember]
// }
