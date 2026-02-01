package com.ghenriqf.schedule.music.dto.response;

import lombok.Builder;

@Builder
public record MusicResponse(
        Long id,
        Long ministryId,
        String title,
        String artist,
        String tone,
        String videoLink,
        String chordSheetLink
) {
}
