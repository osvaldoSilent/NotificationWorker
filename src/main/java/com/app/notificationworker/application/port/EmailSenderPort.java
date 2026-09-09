package com.app.notificationworker.application.port;

public interface EmailSenderPort {
    void sendOtpEmail(String destinationEmail, String otpCode);
}