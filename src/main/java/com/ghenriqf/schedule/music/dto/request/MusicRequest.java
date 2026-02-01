package com.ghenriqf.schedule.music.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record MusicRequest(
        @NotEmpty(message = "Title is required")
        @Size(max = 150, message = "The title must be a maximum of 150 characters")
        String title,

        @NotEmpty(message = "Artist is required")
        @Size(max = 100, message = "The artist's entry must be a maximum of 100 characters")
        String artist,

        @Size(max = 10, message = "The tone should be a maximum of 10 characters.")
        String tone,

        String videoLink,
        String chordSheetLink
) {
}
