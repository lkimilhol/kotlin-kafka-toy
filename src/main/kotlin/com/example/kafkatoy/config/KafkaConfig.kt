package com.example.kafkatoy.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import reactor.kafka.sender.SenderOptions
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*

@Configuration
class KafkaConfig {
    @Value("\${spring.kafka.bootstrap-servers}")
    lateinit var hosts: String

    private val producerProperties: Map<String, Any> = mapOf(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
        ProducerConfig.ACKS_CONFIG to "all",
    )

    @Primary
    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {

        val containerFactory = ConcurrentKafkaListenerContainerFactory<String, String>()
        containerFactory.consumerFactory = consumerFactory()
        containerFactory.isBatchListener = true
        containerFactory.containerProperties.ackMode = ContainerProperties.AckMode.BATCH

        return containerFactory
    }

    @Primary
    @Bean
    fun consumerFactory(): ConsumerFactory<in String, in String> {
        return DefaultKafkaConsumerFactory(consumerProperties())
    }

    @Bean
    fun consumerProperties(): Map<String, Any> {

        val hostName = try {
            InetAddress.getLocalHost().hostName + UUID.randomUUID().toString()
        } catch (e: UnknownHostException) {
            UUID.randomUUID().toString()
        }

        return hashMapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to hosts,
            ConsumerConfig.GROUP_ID_CONFIG to hostName,
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to "true",
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "latest",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java
        )
    }

    private val producerOption: SenderOptions<String, String> = SenderOptions.create(producerProperties)

    @Bean
    @ConditionalOnProperty(value = ["application.kafka.enable"], havingValue = "true")
    fun kafkaProducerTemplate(): ReactiveKafkaProducerTemplate<String, String> = ReactiveKafkaProducerTemplate(producerOption)
}