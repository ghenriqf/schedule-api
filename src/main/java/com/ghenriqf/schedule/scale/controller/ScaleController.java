package com.ghenriqf.schedule.scale.controller;

import com.ghenriqf.schedule.scale.dto.request.ScaleRequest;
import com.ghenriqf.schedule.scale.dto.response.ScaleResponse;
import com.ghenriqf.schedule.scale.service.ScaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScaleController {

    private final ScaleService scaleService;

    @PostMapping("/ministries/{ministryId}/scales")
    public ResponseEntity<ScaleResponse> create (@RequestBody ScaleRequest scaleRequest, @PathVariable Long ministryId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scaleService.create(scaleRequest, ministryId));
    }
}
