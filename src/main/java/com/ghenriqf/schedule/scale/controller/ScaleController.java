package com.ghenriqf.schedule.scale.controller;

import com.ghenriqf.schedule.scale.dto.request.ScaleMemberRequest;
import com.ghenriqf.schedule.scale.dto.request.ScaleRequest;
import com.ghenriqf.schedule.scale.dto.request.ScaleUpdateRequest;
import com.ghenriqf.schedule.scale.dto.response.ScaleResponse;
import com.ghenriqf.schedule.scale.dto.response.ScaleSummaryResponse;
import com.ghenriqf.schedule.scale.service.ScaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScaleController {

    private final ScaleService scaleService;

    @PostMapping("/ministries/{ministryId}/scales")
    public ResponseEntity<ScaleSummaryResponse> create (@Valid @RequestBody ScaleRequest scaleRequest, @PathVariable Long ministryId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scaleService.create(scaleRequest, ministryId));
    }

    @PatchMapping("/ministries/{ministryId}/scales/{scaleId}")
    public ResponseEntity<ScaleResponse> update (@PathVariable Long ministryId, @PathVariable Long scaleId, @RequestBody ScaleUpdateRequest scaleUpdateRequest) {
        return ResponseEntity.ok(scaleService.update(ministryId, scaleId, scaleUpdateRequest));
    }

    @DeleteMapping("/ministries/{ministryId}/scales/{scaleId}")
    public ResponseEntity<ScaleResponse> delete (@PathVariable Long ministryId, @PathVariable Long scaleId) {
        scaleService.delete(ministryId, scaleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/ministries/{ministryId}/scales")
    public ResponseEntity<List<ScaleSummaryResponse>> listAllByAfterDate (
            @PathVariable Long ministryId,
            @RequestParam(required = false) LocalDateTime dateTime
    ) {
        return ResponseEntity.ok().body(scaleService.listAllByAfterDate(ministryId, dateTime));
    }

    @GetMapping("/ministries/{ministryId}/scales/{scaleId}")
    public ResponseEntity<ScaleResponse> findById (@PathVariable Long ministryId, @PathVariable Long scaleId) {
        return ResponseEntity.ok(scaleService.findById(ministryId, scaleId));
    }

    @PostMapping("/scales/{scaleId}/musics/{musicId}")
    public ResponseEntity<ScaleResponse> addMusic (@PathVariable Long scaleId, @PathVariable Long musicId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scaleService.addMusic(musicId, scaleId));
    }

    @DeleteMapping("/scales/{scaleId}/musics/{musicId}")
    public ResponseEntity<ScaleResponse> removeMusic (@PathVariable Long scaleId, @PathVariable Long musicId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(scaleService.removeMusic(scaleId, musicId));
    }

    @PostMapping("/scales/{scaleId}/members/{memberId}")
    public ResponseEntity<ScaleResponse> addMember (
            @PathVariable Long scaleId,
            @PathVariable Long memberId,
            @RequestBody ScaleMemberRequest scaleMemberRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scaleService.addMember(scaleId, memberId, scaleMemberRequest));
    }

    @DeleteMapping("/scales/{scaleId}/members/{memberId}")
    public ResponseEntity<ScaleResponse> removeMember (@PathVariable Long scaleId, @PathVariable Long memberId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(scaleService.removeMember(scaleId, memberId));
    }
}
