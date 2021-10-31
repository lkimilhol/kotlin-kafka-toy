package com.example.kafkatoy.service

import com.example.kafkatoy.domain.KafkaMessage
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service

@Service
class Producer(private val kafkaProducerTemplate: ReactiveKafkaProducerTemplate<String, KafkaMessage>) {

    fun produce(topic: String, kafkaMessage: KafkaMessage) {
        kafkaProducerTemplate.send(topic, kafkaMessage).subscribe()
    }
}