package com.app.notificationworker.infrastructure.entrypoint.otp.dto;

public record OTPGeneratedEvent(
    String email,
    String otpCode,
    String userName
) {}