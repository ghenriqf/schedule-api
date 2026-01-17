package com.ghenriqf.schedule.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record LoginRequest(
        @NotEmpty(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotEmpty(message = "Password is required")
        @Length(min = 8, max = 250, message = "Password must have at least 8 characters")
        String password
) {
}
