package com.ghenriqf.schedule.ministry.service;

import com.ghenriqf.schedule.auth.context.CurrentUserProvider;
import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.common.exception.AccessDeniedException;
import com.ghenriqf.schedule.common.exception.ResourceNotFoundException;
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
import static org.mockito.Mockito.*;

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

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdatingMinistryAndMinistryNotFound () {
        // given
        User user = new User();
        user.setId(1L);

        Member member = new Member();
        member.setRole(MinistryRole.ADMIN);

        Long ministryId = 1L;

        MinistryUpdateRequest request = new MinistryUpdateRequest("", "");

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(user.getId(), ministryId)).willReturn(member);
        given(ministryRepository.findById(1L)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> ministryService.update(1L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ministry not found with id: " + ministryId);

        verify(ministryRepository, never()).save(any());
    }

    @Test
    void shouldNotUpdateMinistryWhenFieldsAreNull () {
        // given
        User user = new User();
        user.setId(1L);

        Member member = new Member();
        member.setRole(MinistryRole.ADMIN);

        Ministry ministry = new Ministry();
        ministry.setId(1L);
        ministry.setName("Ministry");
        ministry.setDescription("Description");

        MinistryUpdateRequest request = new MinistryUpdateRequest(null, null);

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(user.getId(), ministry.getId())).willReturn(member);
        given(ministryRepository.findById(ministry.getId())).willReturn(Optional.of(ministry));
        given(ministryRepository.save(ministry)).willReturn(ministry);

        // when
        MinistryResponse serviceResponse = ministryService.update(ministry.getId(), request);

        // then
        assertThat(ministry.getName()).isEqualTo("Ministry");
        assertThat(ministry.getDescription()).isEqualTo("Description");

        verify(ministryRepository).save(ministry);
        assertThat(serviceResponse).isNotNull();
    }

    @Test
    void shouldDeleteMinistryWhenMemberIsAdmin () {
        // given
        User user = new User();
        user.setId(1L);

        Member member = new Member();
        member.setRole(MinistryRole.ADMIN);

        Ministry ministry = new Ministry();
        ministry.setId(1L);

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(user.getId(), ministry.getId())).willReturn(member);
        given(ministryRepository.findById(ministry.getId())).willReturn(Optional.of(ministry));

        // when
        ministryService.delete(ministry.getId());

        // then
        verify(ministryRepository).delete(ministry);
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenDeletingMinistryAndMemberIsNotAdmin () {
        // given
        User user = new User();
        user.setId(1L);

        Member member = new Member();
        member.setRole(MinistryRole.MEMBER);

        Ministry ministry = new Ministry();
        ministry.setId(1L);

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(user.getId(), ministry.getId())).willReturn(member);

        // when
        // then
        assertThatThrownBy(() -> ministryService.delete(ministry.getId()))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("Only administrators can delete ministry");

        verify(ministryRepository, never()).delete(any());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDeletingMinistryAndMinistryNotFound () {
        // given
        User user = new User();
        user.setId(1L);

        Member member = new Member();
        member.setRole(MinistryRole.ADMIN);

        Ministry ministry = new Ministry();
        ministry.setId(1L);

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(user.getId(), ministry.getId())).willReturn(member);
        given(ministryRepository.findById(1L)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> ministryService.delete(ministry.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ministry not found with id: " + ministry.getId());

        verify(ministryRepository, never()).delete(any());
    }
}