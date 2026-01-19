package com.ghenriqf.schedule.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Builder
public record UserRequest(
        @NotEmpty(message = "Username is required") String username,

        @NotEmpty(message = "Full name is required") String name,

        @NotEmpty(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotEmpty(message = "Password is required")
        @Length(min = 8, max = 250, message = "Password must have at least 8 characters")
        String password,

        @NotNull(message = "Birth data is required") LocalDate birth
){
}
