package com.example.kafkatoy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaToyApplication

fun main(args: Array<String>) {
    runApplication<KafkaToyApplication>(*args)
}
