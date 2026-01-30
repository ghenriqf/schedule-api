package com.ghenriqf.schedule.music.service;

import com.ghenriqf.schedule.music.repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;

    public Long countByMinistryId (Long id) {
        return musicRepository.countByMinistryId(id);
    }
}
