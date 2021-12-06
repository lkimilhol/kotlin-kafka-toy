package com.example.kafkatoy.service

import com.example.kafkatoy.domain.KafkaMessage
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class DLTConsumer(private val kafkaConsumer: KafkaConsumer<String, KafkaMessage>) {
    fun dltConsume() {
        kafkaConsumer.subscribe(setOf("test.DLT"))
        val message = kafkaConsumer.poll(Duration.ofSeconds(1L))
        println(message.count())
        message.forEach { println(it.toString()) }
        kafkaConsumer.commitSync()
        kafkaConsumer.unsubscribe()
    }
}