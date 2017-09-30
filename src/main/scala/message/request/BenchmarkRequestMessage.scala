package yester.message.request

import play.api.libs.json.{Reads, Json, Format}

import yester.lib.{Benchmark, BenchmarkJsonImplicits}

final case class BenchmarkRequestMessage(messageId: String, content: Benchmark) extends ComplexRequestMessage[Benchmark](messageId, content)

object BenchmarkRequestMessageJsonImplicits {
    implicit val benchmarFormat: Format[Benchmark] =  BenchmarkJsonImplicits.bchFmt

    implicit val benchmarkRequestMessageFmt = Json.format[BenchmarkRequestMessage]
    implicit val benchmarkRequestMessageeWrites = Json.writes[BenchmarkRequestMessage]
    implicit val benchmarkRequestMessageReads = Json.reads[BenchmarkRequestMessage]
}
