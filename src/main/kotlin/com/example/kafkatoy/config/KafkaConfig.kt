package com.example.kafkatoy.config

import com.example.kafkatoy.domain.KafkaMessage
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.RecoveringBatchErrorHandler
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.util.backoff.FixedBackOff
import reactor.kafka.sender.SenderOptions
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*

@Configuration
class KafkaConfig {
    private val producerProperties: Map<String, Any> = mapOf(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
        ProducerConfig.ACKS_CONFIG to "all",
    )

    @Bean("kafkaListenerContainerFactory")
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {

        val containerFactory = ConcurrentKafkaListenerContainerFactory<String, String>()
        val recoverer = DeadLetterPublishingRecoverer(kafkaProducerTemplate())
        val recoveringBackOff = RecoveringBatchErrorHandler(recoverer, FixedBackOff(2L, 1))

        containerFactory.consumerFactory = consumerFactory()
        containerFactory.isBatchListener = true
        containerFactory.containerProperties.ackMode = ContainerProperties.AckMode.BATCH
        containerFactory.setBatchErrorHandler(recoveringBackOff)

        return containerFactory
    }

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
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ConsumerConfig.GROUP_ID_CONFIG to hostName,
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to "true",
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "latest",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
            JsonDeserializer.TRUSTED_PACKAGES to "*"
        )
    }

    private val producerFactory: ProducerFactory<String, KafkaMessage> = DefaultKafkaProducerFactory(producerProperties)

    @Bean
    fun kafkaProducerTemplate(): KafkaTemplate<String, KafkaMessage> = KafkaTemplate(producerFactory)

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().registerModule(KotlinModule())
    }

    @Bean
    fun dltConsumerProperties(): Map<String, Any> {
        return hashMapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ConsumerConfig.GROUP_ID_CONFIG to "dlt-consumer-group",
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to "true",
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
            JsonDeserializer.TRUSTED_PACKAGES to "*"
        )
    }

    @Bean
    fun kafkaDLTConsumer(): KafkaConsumer<String, KafkaMessage> = KafkaConsumer(dltConsumerProperties())
}