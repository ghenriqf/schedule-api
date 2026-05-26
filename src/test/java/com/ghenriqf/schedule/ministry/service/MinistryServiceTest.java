package com.ghenriqf.schedule.ministry.service;

import com.ghenriqf.schedule.auth.context.CurrentUserProvider;
import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.common.exception.AccessDeniedException;
import com.ghenriqf.schedule.common.exception.ResourceNotFoundException;
import com.ghenriqf.schedule.file.service.ImgBBService;
import com.ghenriqf.schedule.member.entity.Member;
import com.ghenriqf.schedule.member.service.MemberService;
import com.ghenriqf.schedule.ministry.dto.request.MinistryRequest;
import com.ghenriqf.schedule.ministry.dto.request.MinistryUpdateRequest;
import com.ghenriqf.schedule.ministry.dto.response.MinistryDetailResponse;
import com.ghenriqf.schedule.ministry.dto.response.MinistryResponse;
import com.ghenriqf.schedule.ministry.entity.Ministry;
import com.ghenriqf.schedule.ministry.entity.MinistryRole;
import com.ghenriqf.schedule.ministry.repository.MinistryRepository;
import com.ghenriqf.schedule.music.service.MusicService;
import com.ghenriqf.schedule.scale.service.ScaleService;
import jakarta.transaction.UserTransaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
    @Mock
    private ImgBBService imgBBService;
    @Mock
    private ScaleService scaleService;
    @Mock
    private MusicService musicService;
    @InjectMocks
    private MinistryService ministryService;

    @Test
    void shouldCreateMinistrySuccessfully() {
        // given
        User user = new User();
        user.setId(1L);

        MultipartFile avatarImage = mock(MultipartFile.class);

        Ministry ministry = new Ministry();
        ministry.setId(1L);
        ministry.setName("Louvor");

        MinistryRequest request = new MinistryRequest("Louvor", "Descrição");

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(imgBBService.uploadToImgBB(avatarImage)).willReturn("https://imgbb.com/image.jpg");
        given(ministryRepository.save(any())).willReturn(ministry);
        doNothing().when(memberService).createAdmin(any(), any());

        // when
        MinistryResponse response = ministryService.create(request, avatarImage);

        // then
        assertThat(response).isNotNull();
        verify(imgBBService).uploadToImgBB(avatarImage);
        verify(ministryRepository).save(any());
        verify(memberService).createAdmin(eq(user), any());
    }

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

    @Test
    void shouldGenerateInviteCodeWhenMemberIsAdmin () {
        // given
        User user = new User();
        user.setId(1L);

        Member member = new Member();
        member.setRole(MinistryRole.ADMIN);

        Ministry ministry = new Ministry();
        ministry.setId(1L);

        given(ministryRepository.findById(ministry.getId())).willReturn(Optional.of(ministry));
        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(user.getId(), ministry.getId())).willReturn(member);

        // when
        String inviteCode = ministryService.generateInviteCode(ministry.getId());

        // then
        assertThat(ministry.getInviteCode()).isEqualTo(inviteCode);
        verify(ministryRepository).save(ministry);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenGeneratingInviteCodeAndMinistryNotFound () {
        // given
        Long ministryId = 1L;

        // when
        // then
        assertThatThrownBy(() -> ministryService.generateInviteCode(ministryId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ministry not found");

        verify(ministryRepository, never()).save(any());
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenGeneratingInviteCodeAndMemberIsNotAdmin() {
        // given
        User user = new User();
        user.setId(1L);

        Member member = new Member();
        member.setRole(MinistryRole.MEMBER);

        Ministry ministry = new Ministry();
        ministry.setId(1L);

        given(ministryRepository.findById(ministry.getId())).willReturn(Optional.of(ministry));
        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(user.getId(), ministry.getId())).willReturn(member);

        // when
        // then
        assertThatThrownBy(() -> ministryService.generateInviteCode(ministry.getId()))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("Only administrators can generate the link");

        verify(ministryRepository, never()).save(any());
    }

    @Test
    void shouldReturnMinistryDetailWhenUserIsMember () {
        // given
        User user = new User();
        user.setId(1L);

        Ministry ministry = new Ministry();
        ministry.setId(1L);

        Member member = new Member();

        given(ministryRepository.findById(ministry.getId())).willReturn(Optional.of(ministry));
        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(user.getId(), ministry.getId())).willReturn(member);

        // when
        MinistryDetailResponse response = ministryService.getDetailById(ministry.getId());

        // then
        assertThat(response).isNotNull();
        verify(memberService).countByMinistryId(ministry.getId());
        verify(scaleService).countByMinistryIdAndDateAfter(eq(ministry.getId()), any(LocalDateTime.class));
        verify(musicService).countByMinistryId(ministry.getId());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenGettingDetailAndMinistryNotFound () {
        // given
        Long ministryId = 1L;

        // when
        // then
        assertThatThrownBy(() -> ministryService.getDetailById(ministryId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ministry not found with id: " + ministryId);

        verify(memberService, never()).countByMinistryId(any());
        verify(scaleService, never()).countByMinistryIdAndDateAfter(any(), any());
        verify(musicService, never()).countByMinistryId(any());
    }
}