package com.ghenriqf.schedule.scale.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ScaleRequest(
        @NotEmpty(message = "Name is required") String name,
        String description,
        @Future
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime date,
        Long ministerId
) {
}
