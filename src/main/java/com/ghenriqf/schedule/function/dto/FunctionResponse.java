package com.ghenriqf.schedule.function.dto;

import lombok.Builder;

@Builder
public record FunctionResponse(
        Long id,
        String name
) {
}
