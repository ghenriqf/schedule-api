package com.ghenriqf.schedule.scale.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ScaleSummaryResponse(
        Long id,
        String name,
        String description,
        LocalDateTime date
) {
}
