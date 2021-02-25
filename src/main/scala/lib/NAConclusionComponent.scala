package yester.lib

// import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec


final case class NAConclusionComponent(decision: String, commitHash: Option[String])

object NAConclusionComponent {
	implicit val codec: Codec[NAConclusionComponent] = Codec.codec[NAConclusionComponent]
}

// object NAConclusionComponentJsonImplicits {
//     implicit val naConclCompFmt = Json.format[NAConclusionComponent]
//     implicit val naConclCompWrites = Json.writes[NAConclusionComponent]
//     implicit val naConclCompReads = Json.reads[NAConclusionComponent]
// }
