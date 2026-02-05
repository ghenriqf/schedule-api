package com.ghenriqf.schedule.scale.mapper;

import com.ghenriqf.schedule.scale.dto.request.ScaleRequest;
import com.ghenriqf.schedule.scale.dto.response.ScaleResponse;
import com.ghenriqf.schedule.scale.entity.Scale;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ScaleMapper {

    public static ScaleResponse toResponse (Scale entity) {
        return ScaleResponse.builder().build();
    }

    public static Scale toEntity (ScaleRequest request) {
        return Scale
                .builder()
                .name(request.name())
                .description(request.description())
                .date(request.date())
                .build();
    }
}
