package com.ghenriqf.schedule.auth.dto.response;

import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String username,
        String email
) {
}
