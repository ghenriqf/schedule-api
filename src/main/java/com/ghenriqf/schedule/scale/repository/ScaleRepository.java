package com.ghenriqf.schedule.scale.repository;

import com.ghenriqf.schedule.scale.entity.Scale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ScaleRepository extends JpaRepository<Scale, Long> {
    Long countByMinistryIdAndDateAfter(Long id, LocalDateTime date);
}
