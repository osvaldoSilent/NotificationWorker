package com.app.notificationworker.infrastructure.entrypoint.otp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.app.notificationworker.infrastructure.entrypoint.otp.dto.OTPGeneratedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OTPEventListener {

    private static final Logger log = LoggerFactory.getLogger(OTPEventListener.class);
    private final ObjectMapper objectMapper;

    public OTPEventListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
        topics = "auth.user.registered",
        groupId = "notification-group"
    )
    public void handleOtpEvent(String eventRawJson) {
        try {
            // 1. Convertimos el String JSON a nuestro objeto Java
            OTPGeneratedEvent event = objectMapper.readValue(eventRawJson, OTPGeneratedEvent.class);
            
            // ¡Ahora sí podemos usar las variables por separado!
            log.info("Evento procesado -> Correo: {}, Código OTP: {}", event.email(), event.otpCode());
            
            // 2. Aquí llamaremos al servicio de correos
            // emailService.sendOtpEmail(event.email(), event.otp());

        } catch (Exception e) {
            log.error("Error al procesar el mensaje de Kafka: {}", eventRawJson, e);
        }
    }
}