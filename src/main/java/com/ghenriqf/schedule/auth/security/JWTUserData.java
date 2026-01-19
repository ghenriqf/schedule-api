package com.ghenriqf.schedule.auth.security;

import lombok.Builder;

@Builder
public record JWTUserData(Long id, String email) {
}
