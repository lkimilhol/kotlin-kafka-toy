package com.example.kafkatoy.service

import com.example.kafkatoy.domain.KafkaMessage
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import java.time.Duration
import java.util.*

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@EmbeddedKafka(partitions = 1, brokerProperties = (arrayOf("listeners=PLAINTEXT://localhost:9093", "port=9093", "delete.topic.enable=true")))
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConsumerTest {

    @Autowired
    lateinit var producer: Producer

    val properties = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9093",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
        ConsumerConfig.MAX_POLL_RECORDS_CONFIG to 100,
        ConsumerConfig.GROUP_ID_CONFIG to "test",
        JsonDeserializer.TRUSTED_PACKAGES to "*",
    )

    @Test
    fun `토픽_쌓기_테스트`() {
        // given
        producer.produce("test", KafkaMessage(1L, "first"))
        // when
        val dltConsumer = KafkaConsumer<String, KafkaMessage>(properties)
        dltConsumer.subscribe(Collections.singletonList("test"))
        val poll = dltConsumer.poll(Duration.ofSeconds(10))
        // then
        assertThat(poll.count()).isEqualTo(1)
    }
}