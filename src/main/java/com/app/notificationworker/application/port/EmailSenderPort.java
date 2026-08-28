package com.app.notificationworker.port;

public interface EmailSenderPort {
    void sendOtpEmail(String destinationEmail, String otpCode);
}