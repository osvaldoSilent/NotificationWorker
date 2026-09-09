package com.app.notificationworker.infrastructure.entrypoint.otp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserRegisteredEvent(
    @JsonProperty("user_name")
    String userName,
    String email,
    String otpCode
) {
}
