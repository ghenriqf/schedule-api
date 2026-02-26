package com.ghenriqf.schedule.member.service;

import com.ghenriqf.schedule.auth.context.CurrentUserProvider;
import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.common.exception.AccessDeniedException;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CurrentUserProvider currentUserProvider;
    private final MinistryRepository ministryRepository;

    public void createAdmin (User user, Ministry ministry) {
        Member member = Member
                .builder()
                .user(user)
                .ministry(ministry)
                .role(MinistryRole.ADMIN)
                .build();

        memberRepository.save(member);
    }

    public List<MemberResponse> listByMinistryId (Long ministryId) {
        User user = currentUserProvider.getCurrentUser();
        this.verifyIfUserIsMemberOfMinistry(user.getId(), ministryId);

        return memberRepository.findByMinistryId(ministryId)
                .stream()
                .map(MemberMapper::toResponse)
                .toList();
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

    public Member findByUserIdAndMinistryId (Long userId, Long ministryId) {
        return memberRepository.findByUserIdAndMinistryId(userId, ministryId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
    }

    public void verifyIfUserIsMemberOfMinistry (Long userId, Long ministryId) {
        if (!(memberRepository.existsByUserIdAndMinistryId(userId, ministryId))) {
            throw new AccessDeniedException("Access to this ministry is for members only");
        }
    }

    public Long countByMinistryId (Long id) {
        return memberRepository.countByMinistryId(id);
    }
}
