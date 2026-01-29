package com.ghenriqf.schedule.music.repository;

import com.ghenriqf.schedule.music.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {
    Long countByMinistryId(Long id);
}
