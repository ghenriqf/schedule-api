package com.ghenriqf.schedule.scale.dto.response;

import com.ghenriqf.schedule.member.dto.response.MemberResponse;
import com.ghenriqf.schedule.music.dto.response.MusicResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
public record ScaleResponse(
        Long id,
        Long ministryId,
        String name,
        String description,
        LocalDateTime date,
        Set<ScaleMemberResponse> members,
        List<MusicResponse> musics
) {
}
