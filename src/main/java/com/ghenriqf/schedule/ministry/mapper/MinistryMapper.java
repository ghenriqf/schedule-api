package com.ghenriqf.schedule.ministry.mapper;

import com.ghenriqf.schedule.ministry.dto.request.MinistryRequest;
import com.ghenriqf.schedule.ministry.dto.response.MinistryResponse;
import com.ghenriqf.schedule.ministry.entity.Ministry;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MinistryMapper {

    public static Ministry toEntity (MinistryRequest request, String avatarUrl) {
        return Ministry
                .builder()
                .name(request.name())
                .description(request.description())
                .avatarUrl(avatarUrl)
                .build();
    }

    public static MinistryResponse toResponse (Ministry entity) {
        return MinistryResponse
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .avatarUrl(entity.getAvatarUrl())
                .build();
    }
}
