package com.ghenriqf.schedule.ministry.dto.response;

import lombok.Builder;

@Builder
public record MinistryResponse(
        Long id,
        String name,
        String description
) {
}
