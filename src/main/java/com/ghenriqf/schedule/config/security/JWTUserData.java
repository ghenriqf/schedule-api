package com.ghenriqf.schedule.config.security;

import lombok.Builder;

@Builder
public record JWTUserData(Long id, String email) {
}
