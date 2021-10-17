package com.example.consumer.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class SampleTopicListener {
    val log: Logger = LoggerFactory.getLogger(SampleTopicListener::class.java)

    @KafkaListener(topics = ["test"])
    fun consume(@Payload data: String) {
        log.info("Message: $data")
    }
}
