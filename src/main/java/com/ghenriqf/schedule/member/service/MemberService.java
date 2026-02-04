package com.ghenriqf.schedule.member.service;

import com.ghenriqf.schedule.auth.context.CurrentUserProvider;
import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.common.exception.ResourceNotFoundException;
import com.ghenriqf.schedule.member.dto.response.MemberResponse;
import com.ghenriqf.schedule.member.entity.Member;
import com.ghenriqf.schedule.member.mapper.MemberMapper;
import com.ghenriqf.schedule.member.repository.MemberRepository;
import com.ghenriqf.schedule.ministry.entity.Ministry;
import com.ghenriqf.schedule.ministry.entity.MinistryRole;
import com.ghenriqf.schedule.ministry.repository.MinistryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CurrentUserProvider currentUserProvider;
    private final MinistryRepository ministryRepository;

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

    public MemberResponse create (User user, Ministry ministry) {
        Member member = Member
                .builder()
                .user(user)
                .ministry(ministry)
                .role(MinistryRole.MEMBER)
                .build();

        Member save = memberRepository.save(member);

        return MemberMapper.toResponse(save);
    }

    public void joinMinistry (String inviteCode) {
        User currentUser = currentUserProvider.getCurrentUser();

        Ministry ministry = ministryRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new ResourceNotFoundException("Ministry not found"));

        if (memberRepository.existsByUserIdAndMinistryId(currentUser.getId(), ministry.getId())) {
            return;
        }

        this.create(currentUser, ministry);
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
