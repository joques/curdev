package yester.lib

import play.api.libs.json.{Json, Format}
import com.couchbase.client.scala.implicits.Codec

final case class CommitteeMembers(devCode: String, members: List[String], action: String)

object CommitteeMembers {
	implicit val codec: Codec[CommitteeMembers] = Codec.codec[CommitteeMembers]

	implicit val cmtMembersFmt = Json.format[CommitteeMembers]
    implicit val cmtMembersWrites = Json.writes[CommitteeMembers]
    implicit val cmtMembersReads = Json.reads[CommitteeMembers]
}