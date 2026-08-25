package com.app.eventhandler.infrastructure.provider.otp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.app.eventhandler.infrastructure.entrypoint.otp.dto.OTPGeneratedEvent;

import java.time.Instant;

@Component
public class KafkaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisher.class);
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String otpTopic;

    public KafkaEventPublisher(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${app.kafka.topics.otp-events}") String otpTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.otpTopic = otpTopic;
    }

    public void publishOtpEvent(String email, String otpCode, String username) {
        OTPGeneratedEvent event = new OTPGeneratedEvent(email, otpCode, username, Instant.now().toString());

        // Usamos 'email' como Message Key para garantizar ordenamiento por usuario
        kafkaTemplate.send(otpTopic, email, event)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Evento OTP publicado exitosamente en [{}], partition [{}]", 
                            otpTopic, result.getRecordMetadata().partition());
                } else {
                    log.error("Error al publicar evento OTP para: {}", email, ex);
                }
            });
    }
}