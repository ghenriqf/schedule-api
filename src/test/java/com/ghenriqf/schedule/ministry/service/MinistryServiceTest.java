package com.ghenriqf.schedule.ministry.service;

import com.ghenriqf.schedule.auth.context.CurrentUserProvider;
import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.common.exception.AccessDeniedException;
import com.ghenriqf.schedule.member.entity.Member;
import com.ghenriqf.schedule.member.service.MemberService;
import com.ghenriqf.schedule.ministry.dto.request.MinistryUpdateRequest;
import com.ghenriqf.schedule.ministry.dto.response.MinistryResponse;
import com.ghenriqf.schedule.ministry.entity.Ministry;
import com.ghenriqf.schedule.ministry.entity.MinistryRole;
import com.ghenriqf.schedule.ministry.repository.MinistryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MinistryServiceTest {

    @Mock
    private CurrentUserProvider currentUserProvider;
    @Mock
    private MemberService memberService;
    @Mock
    private MinistryRepository ministryRepository;
    @InjectMocks
    private MinistryService ministryService;

    @Test
    void shouldUpdateMinistryWhenMemberIsAdmin() {
        // given
        User user = new User();
        user.setId(1L);

        Member member = new Member();
        member.setRole(MinistryRole.ADMIN);

        Ministry ministry = new Ministry();
        ministry.setId(1L);

        MinistryUpdateRequest request = new MinistryUpdateRequest("", "");

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(user.getId(), ministry.getId())).willReturn(member);
        given(ministryRepository.findById(ministry.getId())).willReturn(Optional.of(ministry));
        given(ministryRepository.save(ministry)).willReturn(ministry);

        // when
        MinistryResponse response = ministryService.update(ministry.getId(), request);

        // then
        verify(ministryRepository).save(ministry);
        assertThat(response).isNotNull();
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenUpdatingMinistryAndMemberIsNotAdmin () {
        // given
        User user = new User();
        user.setId(1L);

        Member member = new Member();
        member.setRole(MinistryRole.MEMBER);

        MinistryUpdateRequest request = new MinistryUpdateRequest("", "");

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(user.getId(), 1L)).willReturn(member);

        // when
        // then
        assertThatThrownBy(() -> ministryService.update(1L, request))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("Only administrators can update ministry");

        verify(ministryRepository, never()).save(any());
    }
}