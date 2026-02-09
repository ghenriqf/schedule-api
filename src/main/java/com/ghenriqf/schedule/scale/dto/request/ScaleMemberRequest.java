package com.ghenriqf.schedule.scale.dto.request;

import java.util.Set;

public record ScaleMemberRequest(
    Set<Long> functionIds
) {
}
