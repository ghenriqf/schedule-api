package com.ghenriqf.schedule.scale.mapper;

import com.ghenriqf.schedule.function.entity.Function;
import com.ghenriqf.schedule.scale.dto.response.ScaleMemberResponse;
import com.ghenriqf.schedule.scale.entity.ScaleMember;
import lombok.experimental.UtilityClass;

import java.util.stream.Collectors;

@UtilityClass
public class ScaleMemberMapper {

    public static ScaleMemberResponse toResponse (ScaleMember scaleMember) {
        return ScaleMemberResponse
                .builder()
                .id(scaleMember.getId())
                .memberId(scaleMember.getMember().getId())
                .memberName(scaleMember.getMember().getUser().getName())
                .functions(
                        scaleMember.getFunctions()
                                .stream()
                                .map(Function::getName)
                                .collect(Collectors.toSet()))
                .build();
    }
}
