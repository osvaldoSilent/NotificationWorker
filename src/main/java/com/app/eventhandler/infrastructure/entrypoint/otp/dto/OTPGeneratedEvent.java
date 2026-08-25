package com.app.eventhandler.infrastructure.entrypoint.otp.dto;

import java.time.Instant;

public record OTPGeneratedEvent(
    String email,
    String otpCode,
    String user_name,
    String createdAt
) {}