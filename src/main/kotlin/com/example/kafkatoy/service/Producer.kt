package com.example.kafkatoy.service

import com.example.kafkatoy.domain.KafkaMessage
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service

@Service
class Producer(private val kafkaProducerTemplate: ReactiveKafkaProducerTemplate<String, KafkaMessage>) {

    fun produce() {
        val kafkaMessage = KafkaMessage(1L, "test produce")
        kafkaProducerTemplate.send("test", kafkaMessage).subscribe()
    }
}