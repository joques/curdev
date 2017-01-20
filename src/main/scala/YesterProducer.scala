import java.util.{Properties, UUID}
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.StringDeserializer

class YesterProducer() {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("acks", "all")
    props.put("retries", "0")
    props.put("batch.size", "16384")
    props.put("auto.commit.interval.ms", "1000")
    props.put("linger.ms", "0")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("block.on.buffer.full", "true")

    private val producer: KafkaProducer[String,String] = new KafkaProducer[String,String](props)
    def getProducer(): KafkaProducer[String,String] = producer
}
