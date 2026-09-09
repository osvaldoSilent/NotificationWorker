package com.app.notificationworker.infrastructure.entrypoint.otp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.app.notificationworker.application.port.EmailSenderPort;
import com.app.notificationworker.infrastructure.entrypoint.otp.dto.OTPGeneratedEvent;
import com.app.notificationworker.infrastructure.util.Encryption;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OTPEventListener {

    private static final Logger log = LoggerFactory.getLogger(OTPEventListener.class);
    private final ObjectMapper objectMapper;
    private final EmailSenderPort smtpEmailService;
    private final Encryption encryption;

    public OTPEventListener(ObjectMapper objectMapper, EmailSenderPort smtpEmailService, Encryption encryption) {
        this.objectMapper = objectMapper;
        this.smtpEmailService = smtpEmailService;
        this.encryption = encryption;
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
            String decryptedOtpCode = encryption.decrypt(event.otpCode());
            log.info("Evento procesado -> Correo: {}, Código OTP: {}", event.email(), event.otpCode());
            log.info("🎉 ¡MENSAJE RECIBIDO CON ÉXITO DESDE KAFKA!");
            log.info("👤 Usuario ID / Name: {}", event.userName());
            log.info("📧 Correo: {}", event.email());
            log.info("🔑 Código OTP: {}", decryptedOtpCode);

            smtpEmailService.sendOtpEmail(event.email(), decryptedOtpCode);

        } catch (Exception e) {
            log.error("Error al procesar el mensaje de Kafka: {}", eventRawJson, e);
        }
    }
}