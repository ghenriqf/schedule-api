package com.ghenriqf.schedule.member.dto.response;

import com.ghenriqf.schedule.entity.Function;
import lombok.Builder;

import java.util.Set;

@Builder
public record MemberResponse(
        Long id,
        Long userId,
        Long ministryId,
        Set<Function> functions
) {
}
