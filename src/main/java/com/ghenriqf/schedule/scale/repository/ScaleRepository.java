package com.ghenriqf.schedule.scale.repository;

import com.ghenriqf.schedule.scale.entity.Scale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScaleRepository extends JpaRepository<Scale, Long> {
    Long countByMinistryIdAndDateAfter(Long id, LocalDateTime date);
    Optional<Scale> findByIdAndMinistryId(Long scaleId, Long ministryId);
    boolean existsByScaleIdAndMemberId(Long scaleId, Long memberId);
    @Query("SELECT DISTINCT s FROM Scale s " +
            "LEFT JOIN FETCH s.members sm " +
            "LEFT JOIN FETCH sm.functions " +
            "WHERE s.ministry.id = :ministryId " +
            "AND s.date >= :date " +
            "ORDER BY s.date ASC")
    List<Scale> findUpcomingScales(@Param("ministryId") Long ministryId, @Param("date") LocalDateTime date);

}
