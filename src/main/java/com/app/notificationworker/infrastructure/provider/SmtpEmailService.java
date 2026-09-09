package com.app.notificationworker.infrastructure.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.app.notificationworker.application.port.EmailSenderPort;

@Service
public class SmtpEmailService implements EmailSenderPort{

    private static final Logger log = LoggerFactory.getLogger(SmtpEmailService.class);
    private final JavaMailSender mailSender;

    public SmtpEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String destinationEmail, String otpCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("tu_correo_de_prueba@gmail.com");
            message.setTo(destinationEmail);
            message.setSubject("🔒 Tu código de verificación de cuenta");
            message.setText("Hola,\n\nTu código OTP para confirmar tu cuenta es: " + otpCode + 
                            "\n\nEste código expirará en 5 minutos. ¡No lo compartas con nadie!");

            mailSender.send(message);
            log.info("📧 Correo OTP enviado exitosamente a: {}", destinationEmail);
        } catch (Exception e) {
            log.error("❌ Falló el envío de correo a {}: {}", destinationEmail, e.getMessage());
        }
    }
}