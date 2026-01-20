package com.ghenriqf.schedule.ministry.dto.response;

import com.ghenriqf.schedule.member.dto.response.MemberResponse;
import com.ghenriqf.schedule.member.entity.Member;
import lombok.Builder;

import java.util.List;

@Builder
public record MinistryResponse(
        Long id,
        String name,
        String description
) {
}
