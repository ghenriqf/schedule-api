package com.ghenriqf.schedule.scale.dto.response;

import lombok.Builder;

import java.util.Set;

@Builder
public record ScaleMemberResponse(
        Long id,
        Long memberId,
        String memberName,
        Set<String> functions
) {
}
