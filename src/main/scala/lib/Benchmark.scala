package yester.lib

import play.api.libs.json.Json
import com.couchbase.client.scala.implicits.Codec

final case class Benchmark(devCode: String)

object Benchmark {
	implicit val codec: Codec[Benchmark] = Codec.codec[Benchmark]

	implicit val bchFmt = Json.format[Benchmark]
	implicit val bchWrites = Json.writes[Benchmark]
	implicit val bchReads = Json.reads[Benchmark]
}