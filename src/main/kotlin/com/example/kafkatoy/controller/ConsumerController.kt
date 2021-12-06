package com.example.kafkatoy.controller

import com.example.kafkatoy.service.DLTConsumer
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ConsumerController(private val consumer: DLTConsumer) {

    @PostMapping("/")
    fun test() {
        consumer.dltConsume()
    }
}