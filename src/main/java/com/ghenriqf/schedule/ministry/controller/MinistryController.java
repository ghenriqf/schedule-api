package com.ghenriqf.schedule.ministry.controller;

import com.ghenriqf.schedule.ministry.dto.request.MinistryRequest;
import com.ghenriqf.schedule.ministry.dto.response.MinistryDetailResponse;
import com.ghenriqf.schedule.ministry.dto.response.MinistryResponse;
import com.ghenriqf.schedule.ministry.service.MinistryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ministries")
public class MinistryController {

    private final MinistryService ministryService;

    @PostMapping
    public ResponseEntity<MinistryResponse> create(@Valid @RequestBody MinistryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ministryService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<MinistryResponse>> findAll() {
        return ResponseEntity.ok(ministryService.findAllByCurrentUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MinistryDetailResponse> findDetailById (@PathVariable Long id) {
        return ResponseEntity.ok(ministryService.getDetailById(id));
    }
}
