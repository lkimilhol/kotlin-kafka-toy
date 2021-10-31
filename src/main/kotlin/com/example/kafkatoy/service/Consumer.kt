package com.example.kafkatoy.service

import com.example.kafkatoy.domain.KafkaMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch

@Service
class Consumer {

    private var messageList = listOf<KafkaMessage>()

    @KafkaListener(id = "kafkaMessages", topics = ["test"], containerFactory = "kafkaListenerContainerFactory")
    fun consume(kafkaMessages: List<KafkaMessage>) {
        messageList = kafkaMessages
        kafkaMessages.forEach { println(it) }
    }

    fun getPayload(): List<KafkaMessage> {
        return messageList
    }
}
