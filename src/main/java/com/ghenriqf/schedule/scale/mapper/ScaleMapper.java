package com.ghenriqf.schedule.scale.mapper;

import com.ghenriqf.schedule.member.entity.Member;
import com.ghenriqf.schedule.music.mapper.MusicMapper;
import com.ghenriqf.schedule.scale.dto.request.ScaleRequest;
import com.ghenriqf.schedule.scale.dto.response.ScaleResponse;
import com.ghenriqf.schedule.scale.dto.response.ScaleSummaryResponse;
import com.ghenriqf.schedule.scale.entity.Scale;

import lombok.experimental.UtilityClass;

import java.util.stream.Collectors;

@UtilityClass
public class ScaleMapper {

    public static ScaleResponse toResponse (Scale entity) {
        return ScaleResponse
                .builder()
                .id(entity.getId())
                .ministryId(entity.getMinistry().getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .date(entity.getDate())
                .members(entity.getMembers()
                        .stream()
                        .map(ScaleMemberMapper::toResponse)
                        .collect(Collectors.toSet()))
                .musics(entity.getMusics()
                        .stream()
                        .map(MusicMapper::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public static ScaleSummaryResponse toSummaryResponse (Scale entity) {
        return ScaleSummaryResponse
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .date(entity.getDate())
                .build();
    }

    public static Scale toEntity (ScaleRequest request, Member minister) {
        return Scale
                .builder()
                .name(request.name())
                .description(request.description())
                .date(request.date())
                .minister(minister)
                .build();
    }
}
