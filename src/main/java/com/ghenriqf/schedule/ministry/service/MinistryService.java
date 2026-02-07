package com.ghenriqf.schedule.ministry.service;

import com.ghenriqf.schedule.auth.context.CurrentUserProvider;
import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.common.exception.AccessDeniedException;
import com.ghenriqf.schedule.common.exception.ResourceNotFoundException;
import com.ghenriqf.schedule.member.dto.response.MemberResponse;
import com.ghenriqf.schedule.member.entity.Member;
import com.ghenriqf.schedule.member.service.MemberService;
import com.ghenriqf.schedule.ministry.dto.request.MinistryRequest;
import com.ghenriqf.schedule.ministry.dto.response.MinistryDetailResponse;
import com.ghenriqf.schedule.ministry.dto.response.MinistryResponse;
import com.ghenriqf.schedule.ministry.dto.response.MinistryStats;
import com.ghenriqf.schedule.ministry.entity.Ministry;
import com.ghenriqf.schedule.ministry.entity.MinistryRole;
import com.ghenriqf.schedule.ministry.mapper.MinistryMapper;
import com.ghenriqf.schedule.ministry.repository.MinistryRepository;
import com.ghenriqf.schedule.music.service.MusicService;
import com.ghenriqf.schedule.scale.service.ScaleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinistryService {

    private final MinistryRepository ministryRepository;
    private final CurrentUserProvider currentUserProvider;

    private final MemberService memberService;
    private final MusicService musicService;
    private final ScaleService scaleService;

    @Transactional
    public MinistryResponse create (MinistryRequest request) {
        User currentUser = currentUserProvider.getCurrentUser();

        Ministry ministry = Ministry
                .builder()
                .name(request.name())
                .description(request.description())
                .build();

        Ministry save = ministryRepository.save(ministry);

        memberService.createAdmin(currentUser, ministry);

        return MinistryMapper.toResponse(save);
    }

    public String generateInviteCode(Long ministryId) {
        Ministry ministry = ministryRepository.findById(ministryId)
                .orElseThrow(() -> new ResourceNotFoundException("Ministry not found"));

        User currentUser = currentUserProvider.getCurrentUser();
        Member member = memberService.findByUserIdAndMinistryId(currentUser.getId(), ministryId);

        if (!(member.getRole().equals(MinistryRole.ADMIN))) {
            throw new AccessDeniedException("Only administrators can generate the link");
        }

        String inviteCode = UUID.randomUUID().toString();
        ministry.setInviteCode(inviteCode);
        ministryRepository.save(ministry);

        return inviteCode;
    }

    @Transactional
    public List<MinistryResponse> findAllByCurrentUser () {
        User currentUser = currentUserProvider.getCurrentUser();
        return ministryRepository.findMinistriesByUserId(currentUser.getId());
    }

    public MinistryDetailResponse getDetailById (Long id) {
        Ministry ministry = ministryRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Ministry not found with id: " + id));

        User currentUser = currentUserProvider.getCurrentUser();
        Member member = memberService.findByUserIdAndMinistryId(currentUser.getId(), id);

        MinistryStats stats = new MinistryStats(
                memberService.countByMinistryId(id),
                scaleService.countByMinistryIdAndDateAfter(id, LocalDateTime.now()),
                musicService.countByMinistryId(id)
        );

        return new MinistryDetailResponse(
                ministry.getId(),
                ministry.getName(),
                ministry.getDescription(),
                stats,
                member.getRole()
        );
    }
}
