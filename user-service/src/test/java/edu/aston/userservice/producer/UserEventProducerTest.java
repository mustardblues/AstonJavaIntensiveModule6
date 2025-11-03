package edu.aston.userservice.producer;

import org.mockito.Mock;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import edu.aston.event.UserEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class UserEventProducerTest {
    @Mock
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @InjectMocks
    private UserEventProducer producer;

    @Captor
    private ArgumentCaptor<String> topicCaptor;

    @Captor
    private ArgumentCaptor<UserEvent> eventCaptor;

    @Test
    void testSendEvent() {
        producer.sendEvent("CREATE", "test@email.com");

        verify(kafkaTemplate).send(topicCaptor.capture(), eventCaptor.capture());

        String topic = topicCaptor.getValue();
        UserEvent event = eventCaptor.getValue();

        assertThat(topic).isEqualTo("user-events-topic");
        assertThat(event.getAction()).isEqualTo("CREATE");
        assertThat(event.getEmail()).isEqualTo("test@email.com");
    }
}
