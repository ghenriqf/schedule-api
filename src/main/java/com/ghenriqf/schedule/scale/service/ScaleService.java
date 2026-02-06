package com.ghenriqf.schedule.scale.service;

import com.ghenriqf.schedule.auth.context.CurrentUserProvider;
import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.common.exception.AccessDeniedException;
import com.ghenriqf.schedule.common.exception.ResourceNotFoundException;
import com.ghenriqf.schedule.member.dto.response.MemberResponse;
import com.ghenriqf.schedule.member.service.MemberService;
import com.ghenriqf.schedule.ministry.entity.MinistryRole;
import com.ghenriqf.schedule.ministry.repository.MinistryRepository;
import com.ghenriqf.schedule.music.dto.response.MusicResponse;
import com.ghenriqf.schedule.music.entity.Music;
import com.ghenriqf.schedule.music.mapper.MusicMapper;
import com.ghenriqf.schedule.music.repository.MusicRepository;
import com.ghenriqf.schedule.scale.dto.request.ScaleRequest;
import com.ghenriqf.schedule.scale.dto.response.ScaleResponse;
import com.ghenriqf.schedule.scale.entity.Scale;
import com.ghenriqf.schedule.scale.mapper.ScaleMapper;
import com.ghenriqf.schedule.scale.repository.ScaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScaleService {

    private final ScaleRepository scaleRepository;
    private final CurrentUserProvider currentUserProvider;
    private final MemberService memberService;
    private final MinistryRepository ministryRepository;
    private final MusicRepository musicRepository;

    public ScaleResponse create (ScaleRequest scaleRequest, Long ministryId) {
        User currentUser = currentUserProvider.getCurrentUser();
        MemberResponse member = memberService.findByUserIdAndMinistryId(currentUser.getId(), ministryId);

        if (!(member.role().equals(MinistryRole.ADMIN))) {
            throw new AccessDeniedException("Only administrators can create a scale");
        }

        Scale scale = ScaleMapper.toEntity(scaleRequest);

        scale.setMinistry(ministryRepository.findById(ministryId)
                .orElseThrow(() -> new ResourceNotFoundException("Ministry not found with id: " + ministryId)));

        Scale save = scaleRepository.save(scale);

        return ScaleMapper.toResponse(save);
    }

    public MusicResponse addMusic (Long musicId, Long scaleId) {
        Scale scale = scaleRepository.findById(scaleId)
                .orElseThrow(() -> new ResourceNotFoundException("Scale not found"));

        User currentUser = currentUserProvider.getCurrentUser();
        MemberResponse member = memberService.findByUserIdAndMinistryId(currentUser.getId(), scale.getMinistry().getId());

        if (!(scale.getLeader().getId().equals(member.id()) || member.role().equals(MinistryRole.ADMIN))) {
            throw new AccessDeniedException("Only the leader can add song");
        }

        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));

        scale.getMusics().add(music);
        scaleRepository.save(scale);

        return MusicMapper.toResponse(music);
    }

    public MusicResponse removeMusic (Long musicId, Long scaleId) {
        Scale scale = scaleRepository.findById(scaleId)
                .orElseThrow(() -> new ResourceNotFoundException("Scale not found"));

        User currentUser = currentUserProvider.getCurrentUser();
        MemberResponse member = memberService.findByUserIdAndMinistryId(currentUser.getId(), scale.getMinistry().getId());

        if (!(scale.getLeader().getId().equals(member.id()) || member.role().equals(MinistryRole.ADMIN))) {
            throw new AccessDeniedException("Only the leader can remove song");
        }

        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found"));

        scale.getMusics().remove(music);
        scaleRepository.save(scale);

        return MusicMapper.toResponse(music);
    }

    public Long countByMinistryIdAndDateAfter (Long id, LocalDateTime date) {
        return scaleRepository.countByMinistryIdAndDateAfter(id, date);
    }
}

