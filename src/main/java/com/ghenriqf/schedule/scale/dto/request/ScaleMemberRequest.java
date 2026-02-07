    package com.ghenriqf.schedule.scale.dto.request;

    import java.util.Set;

    public record ScaleMemberRequest(
            Long memberId,
            Set<Long> functionIds
    ) {
    }
