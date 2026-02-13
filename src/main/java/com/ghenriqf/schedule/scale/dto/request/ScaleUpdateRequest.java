package com.ghenriqf.schedule.scale.dto.request;

import java.time.LocalDateTime;

public record ScaleUpdateRequest(
        String name,
        String description,
        LocalDateTime date,
        Long ministerId
) {
}
