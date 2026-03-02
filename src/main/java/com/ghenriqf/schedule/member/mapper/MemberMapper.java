package com.ghenriqf.schedule.member.mapper;

import com.ghenriqf.schedule.auth.mapper.UserMapper;
import com.ghenriqf.schedule.member.dto.response.MemberResponse;
import com.ghenriqf.schedule.member.entity.Member;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MemberMapper {

    public static MemberResponse toResponse (Member entity) {
        return MemberResponse
                .builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .ministryId(entity.getMinistry().getId())
                .role(entity.getRole())
                .user(UserMapper.toResponse(entity.getUser()))
                .build();
    }
}
