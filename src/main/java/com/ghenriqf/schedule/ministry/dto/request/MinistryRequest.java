package com.ghenriqf.schedule.ministry.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record MinistryRequest(
        @NotEmpty(message = "Name is required")
        String name,
        String description
) {
}
