package com.ghenriqf.schedule.music.mapper;

import com.ghenriqf.schedule.music.dto.request.MusicRequest;
import com.ghenriqf.schedule.music.dto.response.MusicResponse;
import com.ghenriqf.schedule.music.entity.Music;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MusicMapper {

    public static MusicResponse toResponse (Music entity) {
        return MusicResponse
                .builder()
                .id(entity.getId())
                .ministryId(entity.getMinistry().getId())
                .title(entity.getTitle())
                .artist(entity.getArtist())
                .tone(entity.getTone())
                .videoLink(entity.getVideoLink())
                .chordSheetLink(entity.getChordSheetLink())
                .build();
    }

    public static Music toEntity (MusicRequest request) {
        return Music
                .builder()
                .title(request.title())
                .artist(request.artist())
                .tone(request.tone())
                .videoLink(request.videoLink())
                .chordSheetLink(request.chordSheetLink())
                .build();
    }
}
