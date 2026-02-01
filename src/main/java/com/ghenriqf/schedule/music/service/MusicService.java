package com.ghenriqf.schedule.music.service;

import com.ghenriqf.schedule.auth.context.CurrentUserProvider;
import com.ghenriqf.schedule.auth.entity.User;
import com.ghenriqf.schedule.exception.AccessDeniedException;
import com.ghenriqf.schedule.exception.ResourceNotFoundException;
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

    public Long countByMinistryId (Long id) {
        return musicRepository.countByMinistryId(id);
    }
}
