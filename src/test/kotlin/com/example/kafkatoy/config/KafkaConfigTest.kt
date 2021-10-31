package com.example.kafkatoy.config

import com.example.kafkatoy.domain.KafkaMessage
import com.example.kafkatoy.service.Consumer
import com.example.kafkatoy.service.Producer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import java.lang.Thread.sleep

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = (arrayOf("listeners=PLAINTEXT://localhost:9093", "port=9093")))
internal class KafkaConfigTest {

    @Autowired
    lateinit var consumer: Consumer

    @Autowired
    lateinit var producer: Producer

    @Test
    fun `설정_BATCH_확인`() {
        // given
        val kafkaMessage = KafkaMessage(1L, "test produce")
        producer.produce("test", kafkaMessage)
        // when
        sleep(1000)
        val payload = consumer.getPayload()
        // then
        assertThat(payload.size).isEqualTo(1)
    }
}