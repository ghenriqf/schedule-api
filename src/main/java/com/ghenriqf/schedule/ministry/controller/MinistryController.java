package com.ghenriqf.schedule.ministry.controller;

import com.ghenriqf.schedule.member.service.MemberService;
import com.ghenriqf.schedule.ministry.dto.request.MinistryRequest;
import com.ghenriqf.schedule.ministry.dto.request.MinistryUpdateRequest;
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
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MinistryResponse> create(@Valid @RequestBody MinistryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ministryService.create(request));
    }

    @PatchMapping("/{ministryId}")
    ResponseEntity<MinistryResponse> update (@PathVariable Long ministryId,@RequestBody MinistryUpdateRequest ministryUpdateRequest) {
        return ResponseEntity.ok(ministryService.update(ministryId, ministryUpdateRequest));
    }

    @DeleteMapping("/{ministryId}")
    ResponseEntity<MinistryResponse> delete (@PathVariable Long ministryId) {
        ministryService.delete(ministryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<MinistryResponse>> findAll() {
        return ResponseEntity.ok(ministryService.findAllByCurrentUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MinistryDetailResponse> findDetailById (@PathVariable Long id) {
        return ResponseEntity.ok(ministryService.getDetailById(id));
    }

    @PostMapping("/{id}/invite-code")
    public ResponseEntity<String> generateInviteCode (@PathVariable Long id) {
        return ResponseEntity.ok(ministryService.generateInviteCode(id));
    }

    @PostMapping("/join/{inviteCode}")
    public ResponseEntity<Void> join(@PathVariable String inviteCode) {
        memberService.joinMinistry(inviteCode);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
