package com.ghenriqf.schedule.music.controller;

import com.ghenriqf.schedule.music.dto.request.MusicRequest;
import com.ghenriqf.schedule.music.dto.response.MusicResponse;
import com.ghenriqf.schedule.music.service.MusicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;

    @PostMapping("/ministries/{ministryId}/musics")
    public ResponseEntity<MusicResponse> create (@Valid @RequestBody MusicRequest musicRequest, @PathVariable Long ministryId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(musicService.save(ministryId, musicRequest));
    }
}