package com.ghenriqf.schedule.member.service;

import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.common.exception.ResourceNotFoundException;
import com.ghenriqf.schedule.member.dto.response.MemberResponse;
import com.ghenriqf.schedule.member.entity.Member;
import com.ghenriqf.schedule.member.mapper.MemberMapper;
import com.ghenriqf.schedule.member.repository.MemberRepository;
import com.ghenriqf.schedule.ministry.entity.Ministry;
import com.ghenriqf.schedule.ministry.entity.MinistryRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse createAdmin (User user, Ministry ministry) {
        Member member = Member
                .builder()
                .user(user)
                .ministry(ministry)
                .role(MinistryRole.ADMIN)
                .build();

        Member save = memberRepository.save(member);

        return MemberMapper.toResponse(save);
    }

    public Long countByMinistryId (Long id) {
        return memberRepository.countByMinistryId(id);
    }

    public MemberResponse findByUserIdAndMinistryId (Long userId, Long ministryId) {
        Member member = memberRepository.findByUserIdAndMinistryId(userId, ministryId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        return MemberMapper.toResponse(member);
    }
}
