package com.ghenriqf.schedule.music.controller;

import com.ghenriqf.schedule.common.dto.PageRequestDTO;
import com.ghenriqf.schedule.music.dto.request.MusicRequest;
import com.ghenriqf.schedule.music.dto.response.MusicResponse;
import com.ghenriqf.schedule.music.service.MusicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;

    @PostMapping("/ministries/{ministryId}/musics")
    public ResponseEntity<MusicResponse> create (@Valid @RequestBody MusicRequest musicRequest, @PathVariable Long ministryId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(musicService.save(ministryId, musicRequest));
    }

    @DeleteMapping("/ministries/{ministryId}/musics")
    public ResponseEntity<MusicResponse> delete (@PathVariable Long musicId, @PathVariable Long ministryId) {
        musicService.delete(musicId, ministryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ministries/{ministryId}/musics")
    public ResponseEntity<List<MusicResponse>> listAll (@Valid PageRequestDTO pageRequestDTO, @PathVariable Long id ) {
        return ResponseEntity.ok(musicService.findAll(pageRequestDTO, id));
    }
}