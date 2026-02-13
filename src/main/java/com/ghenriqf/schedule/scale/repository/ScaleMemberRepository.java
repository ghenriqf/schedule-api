package com.ghenriqf.schedule.scale.repository;

import com.ghenriqf.schedule.scale.entity.ScaleMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScaleMemberRepository extends JpaRepository<ScaleMember, Long> {
    boolean existsByScaleIdAndMemberId(Long scaleId, Long memberId);
    Optional<ScaleMember> findByScaleIdAndMemberId(Long scaleId, Long memberId);
}
