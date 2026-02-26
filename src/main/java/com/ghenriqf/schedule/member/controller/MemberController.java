package com.ghenriqf.schedule.member.controller;

import com.ghenriqf.schedule.member.dto.response.MemberResponse;
import com.ghenriqf.schedule.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/ministries/{ministryId}/members")
    public ResponseEntity<List<MemberResponse>> listByMinistryId (@PathVariable Long ministryId) {
        return ResponseEntity.ok(memberService.listByMinistryId(ministryId));
    }
}
