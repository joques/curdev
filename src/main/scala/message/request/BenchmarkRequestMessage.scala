package yester.message.request

import play.api.libs.json.{Reads, Json, Format}
import com.couchbase.client.scala.implicits.Codec

import yester.lib.Benchmark

final case class BenchmarkRequestMessage(messageId: String, content: Benchmark) extends ComplexRequestMessage[Benchmark](messageId, content)

object BenchmarkRequestMessage {
	implicit val codec: Codec[BenchmarkRequestMessage] = Codec.codec[BenchmarkRequestMessage]

	implicit val benchmarkRequestMessageFmt = Json.format[BenchmarkRequestMessage]
    implicit val benchmarkRequestMessageeWrites = Json.writes[BenchmarkRequestMessage]
    implicit val benchmarkRequestMessageReads = Json.reads[BenchmarkRequestMessage]
}