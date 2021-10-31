package com.example.kafkatoy.controller

import com.example.kafkatoy.domain.KafkaMessage
import com.example.kafkatoy.service.Producer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ProducerController(private val producer: Producer) {

    @GetMapping("/")
    fun test() {
        producer.produce("test", KafkaMessage(1L, "test"))
    }
}