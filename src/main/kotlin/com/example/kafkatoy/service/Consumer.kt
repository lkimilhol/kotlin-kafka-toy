package com.example.kafkatoy.service

import com.example.kafkatoy.domain.KafkaMessage
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.BatchListenerFailedException
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch

@Service
class Consumer {
    private var messageList = listOf<KafkaMessage>()

    @KafkaListener(id = "kafkaMessages", topics = ["test"], containerFactory = "kafkaListenerContainerFactory")
    fun consume(kafkaMessages: ConsumerRecords<String, KafkaMessage>) {
        throw BatchListenerFailedException("failed", 0)
        kafkaMessages.forEach { println(it) }
    }

    fun getPayload(): List<KafkaMessage> {
        return messageList
    }

    fun consumeDLT() {

    }
}
