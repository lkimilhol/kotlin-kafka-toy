package com.example.kafkatoy.service

import com.example.kafkatoy.domain.KafkaMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class Consumer {
    val log: Logger = LoggerFactory.getLogger(Consumer::class.java)

    @KafkaListener(id = "kafkaMessages", topics = ["test"], containerFactory = "kafkaListenerContainerFactory")
    fun consume(kafkaMessages: List<KafkaMessage>) {
        kafkaMessages.forEach { kafkaMessage -> println(kafkaMessage.toString()) }
    }
}
