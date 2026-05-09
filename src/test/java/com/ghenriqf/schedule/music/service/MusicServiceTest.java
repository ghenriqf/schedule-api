package com.ghenriqf.schedule.music.service;

import com.ghenriqf.schedule.auth.context.CurrentUserProvider;
import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.common.exception.AccessDeniedException;
import com.ghenriqf.schedule.common.exception.ResourceNotFoundException;
import com.ghenriqf.schedule.member.entity.Member;
import com.ghenriqf.schedule.member.service.MemberService;
import com.ghenriqf.schedule.ministry.entity.Ministry;
import com.ghenriqf.schedule.ministry.entity.MinistryRole;
import com.ghenriqf.schedule.ministry.repository.MinistryRepository;
import com.ghenriqf.schedule.music.dto.request.MusicRequest;
import com.ghenriqf.schedule.music.dto.response.MusicResponse;
import com.ghenriqf.schedule.music.entity.Music;
import com.ghenriqf.schedule.music.repository.MusicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MusicServiceTest {

    @Mock
    private CurrentUserProvider currentUserProvider;
    @Mock
    private MusicRepository musicRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private MinistryRepository ministryRepository;
    @InjectMocks
    private MusicService musicService;

    @Test
    void shouldSaveMusicSuccessfullyWhenSavingAndMemberIsAdmin() {
        // given
        MusicRequest musicRequest = new MusicRequest("", "", "", "", "");

        User user = new User();
        user.setId(1L);

        Member member = new Member();
        member.setRole(MinistryRole.ADMIN);

        Ministry ministry = new Ministry();
        ministry.setId(1L);

        Music music = new Music();
        music.setMinistry(ministry);

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(1L, 1L)).willReturn(member);
        given(ministryRepository.findById(1L)).willReturn(Optional.of(ministry));
        given(musicRepository.save(any())).willReturn(music);

        // when
        MusicResponse response = musicService.save(1L, musicRequest);

        // then
        assertThat(response).isNotNull();
        verify(musicRepository).save(any());
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenSavingAndMemberIsNotAdmin() {
        // given
        User user = new User();
        user.setId(1L);

        Member member = new Member();
        member.setRole(MinistryRole.MEMBER);

        MusicRequest musicRequest = new MusicRequest("", "", "", "", "");

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(1L, 1L)).willReturn(member);

        // when
        // then
        assertThatThrownBy(() -> musicService.save(1L, musicRequest))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("Only administrators can add song");

        verify(musicRepository, never()).save(any());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenSavingAndMinistryNotFound() {
        // given
        User user = new User();
        user.setId(1L);

        Member member = new Member();
        member.setRole(MinistryRole.ADMIN);

        MusicRequest musicRequest = new MusicRequest("", "", "", "", "");

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(1L, 1L)).willReturn(member);
        given(ministryRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> musicService.save(1L, musicRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ministry not found");

        verify(musicRepository, never()).save(any());
    }

    @Test
    void shouldReturnMusicWhenFindingByIdAndIdIsValid() {
        // given
        User user = new User();
        user.setId(1L);

        Ministry ministry = new Ministry();
        ministry.setId(1L);

        Music music = new Music();
        music.setId(1L);
        music.setMinistry(ministry);

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        doNothing().when(memberService).verifyIfUserIsMemberOfMinistry(1L, 1L);
        given(musicRepository.findById(1L)).willReturn(Optional.of(music));

        // when
        MusicResponse response = musicService.findById(music.getId(), ministry.getId());

        // then
        assertThat(response).isNotNull();
        verify(musicRepository).findById(1L);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenFindingByIdAndSongNotFound() {
        // given
        User user = new User();
        user.setId(1L);

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        doNothing().when(memberService).verifyIfUserIsMemberOfMinistry(user.getId(), 1L);
        given(musicRepository.findById(1L)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> musicService.findById(1L, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Song not found");
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenFindingByIdAndSongDoesNotBelongToMinistry () {
        // given
        User user = new User();
        user.setId(1L);

        Ministry ministry = new Ministry();
        ministry.setId(1L);
        Music music = new Music();
        music.setId(1L);
        music.setMinistry(ministry);

        Long anotherMinistryId  = 2L;

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(musicRepository.findById(music.getId())).willReturn(Optional.of(music));
        doNothing().when(memberService).verifyIfUserIsMemberOfMinistry(user.getId(), anotherMinistryId );

        // when
        // then
        assertThatThrownBy(() -> musicService.findById(music.getId(), anotherMinistryId ))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("This song does not belong to the ministry mentioned");
    }

    @Test
    void shouldDeleteSongSuccessfullyWhenMemberIsAdmin () {
        // given
        User user = new User();
        user.setId(1L);

        Ministry ministry = new Ministry();
        ministry.setId(1L);

        Member member = new Member();
        member.setRole(MinistryRole.ADMIN);

        Music music = new Music();
        music.setMinistry(ministry);
        music.setId(1L);

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(user.getId(), ministry.getId())).willReturn(member);
        given(musicRepository.findById(music.getId())).willReturn(Optional.of(music));

        // when
        musicService.delete(music.getId(), ministry.getId());

        // then
        verify(musicRepository).delete(music);
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenDeletingSongAndMemberIsNotAdmin () {
        // given
        User user = new User();
        user.setId(1L);

        Member member = new Member();
        member.setRole(MinistryRole.MEMBER);

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(user.getId(), 1L)).willReturn(member);

        // when
        // then
        assertThatThrownBy(() -> musicService.delete(1L, 1L))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("Only administrators can delete songs");

        verify(musicRepository, never()).delete(any());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDeletingAndSongNotFound () {
        // given
        User user = new User();
        user.setId(1L);

        Member member = new Member();
        member.setRole(MinistryRole.ADMIN);

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(user.getId(), 1L)).willReturn(member);

        // when
        // then
        assertThatThrownBy(() -> musicService.delete(1L, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Song not found");

        verify(musicRepository, never()).delete(any());
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenDeletingAndSongDoesNotBelongToMinistry () {
        // given
        User user = new User();
        user.setId(1L);

        Member member = new Member();
        member.setRole(MinistryRole.ADMIN);

        Ministry ministry = new Ministry();
        ministry.setId(1L);

        Music music = new Music();
        music.setId(1L);
        music.setMinistry(ministry);

        Long anotherMinistryId  = 2L;


        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(user.getId(), anotherMinistryId)).willReturn(member);
        given(musicRepository.findById(music.getId())).willReturn(Optional.of(music));

        // when
        // then
        assertThatThrownBy(() -> musicService.delete(music.getId(), anotherMinistryId))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("This song does not belong to the ministry mentioned");

        verify(musicRepository, never()).delete(any());
    }

    @Test
    void shouldUpdateSongSuccessfullyWhenMemberIsAdmin () {
        // given
        User user = new User();
        user.setId(1L);

        Ministry ministry = new Ministry();
        ministry.setId(1L);

        Member member = new Member();
        member.setRole(MinistryRole.ADMIN);
        member.setMinistry(ministry);

        Music music = new Music();
        music.setId(1L);
        music.setMinistry(ministry);

        MusicRequest musicRequest = new MusicRequest("", "", "", "", "");

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(user.getId(), ministry.getId())).willReturn(member);
        given(musicRepository.findById(music.getId())).willReturn(Optional.of(music));
        given(musicRepository.save(any())).willReturn(music);

        // when
        MusicResponse response = musicService.update(musicRequest, music.getId(), ministry.getId());

        // then
        verify(musicRepository).save(any());
        assertThat(response).isNotNull();
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenUpdatingSongAndMemberIsNotAdmin () {
        // given
        User user = new User();
        user.setId(1L);

        Member member = new Member();
        member.setRole(MinistryRole.MEMBER);

        MusicRequest musicRequest = new MusicRequest("", "", "", "", "");

        given(currentUserProvider.getCurrentUser()).willReturn(user);
        given(memberService.findByUserIdAndMinistryId(user.getId(), 1L)).willReturn(member);

        // when
        // then
        assertThatThrownBy(() -> musicService.update(musicRequest, 1L, 1L))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("Only administrators can update songs");

        verify(musicRepository, never()).save(any());
    }
}