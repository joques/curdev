package yester.lib

import play.api.libs.json.Json

final case class Benchmark(devCode: String)

object BenchmarkJsonImplicits {
    implicit val bchFmt = Json.format[Benchmark]
    implicit val bchWrites = Json.writes[Benchmark]
    implicit val bchReads = Json.reads[Benchmark]
}
