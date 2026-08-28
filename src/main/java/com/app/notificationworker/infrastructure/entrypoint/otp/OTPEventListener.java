package com.app.notificationworker.infrastructure.entrypoint.otp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.app.notificationworker.infrastructure.entrypoint.otp.dto.OTPGeneratedEvent;
import com.app.notificationworker.port.EmailSenderPort;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OTPEventListener {

    private static final Logger log = LoggerFactory.getLogger(OTPEventListener.class);
    private final ObjectMapper objectMapper;
    private final EmailSenderPort smtpEmailService;
    public OTPEventListener(ObjectMapper objectMapper, EmailSenderPort smtpEmailService) {
        this.objectMapper = objectMapper;
        this.smtpEmailService = smtpEmailService;
    }

    @KafkaListener(
        topics = "auth.user.registered",
        groupId = "notification-group"
    )

    @KafkaListener(topics = "auth.user.registered", groupId = "notification-group")
    public void handleOtpEvent(String eventRawJson) {
        try {
            // 1. Convertimos el String JSON a nuestro objeto Java
            OTPGeneratedEvent event = objectMapper.readValue(eventRawJson, OTPGeneratedEvent.class);
            
            log.info("Evento procesado -> Correo: {}, Código OTP: {}", event.email(), event.otpCode());
            log.info("🎉 ¡MENSAJE RECIBIDO CON ÉXITO DESDE KAFKA!");
            log.info("👤 Usuario ID / Name: {}", event.user_name()); // o el nombre de campo que tenga tu DTO
            log.info("📧 Correo: {}", event.email());
            log.info("🔑 Código OTP: {}", event.otpCode());
            smtpEmailService.sendOtpEmail(event.email(), event.otpCode());

        } catch (Exception e) {
            log.error("Error al procesar el mensaje de Kafka: {}", eventRawJson, e);
        }
    }
}