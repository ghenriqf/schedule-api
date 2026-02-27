package com.ghenriqf.schedule.ministry.dto.response;

import com.ghenriqf.schedule.ministry.entity.MinistryRole;

public record MinistryDetailResponse(
        Long id,
        String name,
        String description,
        String avatarUrl,
        MinistryStats ministryStats,
        MinistryRole role,
        String inviteCode
) {
}
