package com.ghenriqf.schedule.ministry.service;

import com.ghenriqf.schedule.auth.context.CurrentUserProvider;
import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.member.service.MemberService;
import com.ghenriqf.schedule.ministry.dto.request.MinistryRequest;
import com.ghenriqf.schedule.ministry.dto.response.MinistryResponse;
import com.ghenriqf.schedule.ministry.entity.Ministry;
import com.ghenriqf.schedule.ministry.mapper.MinistryMapper;
import com.ghenriqf.schedule.ministry.repository.MinistryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MinistryService {

    private final MinistryRepository ministryRepository;
    private final MemberService memberService;
    private final CurrentUserProvider currentUserProvider;

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

    @Transactional
    public List<MinistryResponse> findAllByCurrentUser () {
        User currentUser = currentUserProvider.getCurrentUser();
        return ministryRepository.findMinistriesByUserId(currentUser.getId());
    }
}
