package com.example.producer

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import java.util.concurrent.Future

fun main(args: Array<String>) {
    var producerRecord : ProducerRecord<String, String> = ProducerRecord("test", "key", "hello!!!")
    val map = mutableMapOf<String, String>()
    map["key.serializer"]   = "org.apache.kafka.common.serialization.StringSerializer"
    map["value.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
    map["bootstrap.servers"] = "localhost:9092"
    var producer = KafkaProducer<String, String>(map as Map<String, Any>?)

    for (i in 1..10) {
        var future: Future<RecordMetadata> = producer.send(producerRecord)!!
        future.get()
    }
}
