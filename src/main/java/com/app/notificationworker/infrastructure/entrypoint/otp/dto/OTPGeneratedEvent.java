package com.app.notificationworker.infrastructure.entrypoint.otp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OTPGeneratedEvent(
    String email,
    String otpCode,
    @JsonProperty("user_name")
    String userName
) {}