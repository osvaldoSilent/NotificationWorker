package com.app.eventhandler.application.otp;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.app.eventhandler.infrastructure.entrypoint.otp.dto.OTPGeneratedEvent;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;

@Service
public class OTPService {

    private final StringRedisTemplate redisTemplate;
    private final KafkaTemplate<String, OTPGeneratedEvent> kafkaTemplate;

    private static final String OTP_PREFIX = "OTP:";
    private static final String KAFKA_TOPIC = "notification.OTP.v1";
    private static final SecureRandom secureRandom = new SecureRandom();

    public OTPService(StringRedisTemplate redisTemplate, KafkaTemplate<String, OTPGeneratedEvent> kafkaTemplate) {
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void generateAndPublishOTP(String email, String username) {
         System.out.println("evento dispirado!");
        // 1. Generar OTP
        int code = 100000 + secureRandom.nextInt(900000);
        String OTPCode = String.valueOf(code);

        // 2. Guardar en Redis con expiración de 5 minutos
        String key = OTP_PREFIX + email.toLowerCase().trim();
        redisTemplate.opsForValue().set(key, OTPCode, Duration.ofMinutes(5));

        // 3. Publicar evento a Kafka de forma asíncrona
        OTPGeneratedEvent event = new OTPGeneratedEvent(email, OTPCode, username, Instant.now().toString());
        kafkaTemplate.send(KAFKA_TOPIC, email, event);
        System.out.println("evento dispirado!");
    }

    public boolean validateOTP(String email, String inputCode) {
        String key = OTP_PREFIX + email.toLowerCase().trim();
        String storedOTP = redisTemplate.opsForValue().get(key);

        if (storedOTP != null && storedOTP.equals(inputCode)) {
            redisTemplate.delete(key); // Uso único
            return true;
        }

        return false;
    }
}