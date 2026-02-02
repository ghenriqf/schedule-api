package com.ghenriqf.schedule.music.service;

import com.ghenriqf.schedule.auth.context.CurrentUserProvider;
import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.common.dto.PageRequestDTO;
import com.ghenriqf.schedule.common.exception.AccessDeniedException;
import com.ghenriqf.schedule.common.exception.ResourceNotFoundException;
import com.ghenriqf.schedule.member.dto.response.MemberResponse;
import com.ghenriqf.schedule.member.service.MemberService;
import com.ghenriqf.schedule.ministry.entity.MinistryRole;
import com.ghenriqf.schedule.ministry.repository.MinistryRepository;
import com.ghenriqf.schedule.music.dto.request.MusicRequest;
import com.ghenriqf.schedule.music.dto.response.MusicResponse;
import com.ghenriqf.schedule.music.entity.Music;
import com.ghenriqf.schedule.music.mapper.MusicMapper;
import com.ghenriqf.schedule.music.repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;
    private final CurrentUserProvider currentUserProvider;
    private final MemberService memberService;
    private final MinistryRepository ministryRepository;

    public MusicResponse save (Long ministryId, MusicRequest musicRequest) {

        User user = currentUserProvider.getCurrentUser();
        MemberResponse member = memberService.findByUserIdAndMinistryId(user.getId(), ministryId);

        if (!(member.role().equals(MinistryRole.ADMIN))) {
            throw new AccessDeniedException("Only administrators can add music");
        }

        Music music = MusicMapper.toEntity(musicRequest);

        music.setMinistry(ministryRepository.findById(ministryId)
                .orElseThrow(() -> new ResourceNotFoundException("Ministry not found")));

        Music saved = musicRepository.save(music);

        return MusicMapper.toResponse(saved);
    }

    public void delete(Long musicId, Long ministryId) {
        User user = currentUserProvider.getCurrentUser();
        MemberResponse member = memberService.findByUserIdAndMinistryId(user.getId(), ministryId);

        if (member.role() != MinistryRole.ADMIN) {
            throw new AccessDeniedException("Only administrators can delete songs");
        }

        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new ResourceNotFoundException("Music not found"));

        if (!music.getMinistry().getId().equals(ministryId)) {
            throw new AccessDeniedException("This song does not belong to the ministry mentioned");
        }

        musicRepository.delete(music);
    }

    public MusicResponse update (MusicRequest musicRequest, Long musicId , Long ministryId) {
        User user = currentUserProvider.getCurrentUser();
        MemberResponse member = memberService.findByUserIdAndMinistryId(user.getId(), ministryId);

        if (member.role() != MinistryRole.ADMIN) {
            throw new AccessDeniedException("Only administrators can update songs");
        }

        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new ResourceNotFoundException("Music not found"));

        if (!music.getMinistry().getId().equals(ministryId)) {
            throw new AccessDeniedException("This song does not belong to the ministry mentioned");
        }

        MusicMapper.updateEntityFromRequest(musicRequest, music);

        Music updatedMusic = musicRepository.save(music);

        return MusicMapper.toResponse(updatedMusic);
    }

    public List<MusicResponse> findAll (PageRequestDTO pageRequestDTO, Long ministryId) {

        User user = currentUserProvider.getCurrentUser();
        MemberResponse member = memberService.findByUserIdAndMinistryId(user.getId(), ministryId);

        if (!(member.ministryId().equals(ministryId))) {
            throw new AccessDeniedException("User does not belong to the ministry");
        }

        return musicRepository.findAll(pageRequestDTO.toPageable())
                .stream()
                .map(MusicMapper::toResponse)
                .toList();
    }

    public Long countByMinistryId (Long id) {
        return musicRepository.countByMinistryId(id);
    }
}
