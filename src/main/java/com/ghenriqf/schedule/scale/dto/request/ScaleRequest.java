package com.ghenriqf.schedule.scale.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ScaleRequest(
        @NotEmpty(message = "Name is required") String name,
        String description,
        @Future LocalDateTime date,
        Long leaderId
) {
}
