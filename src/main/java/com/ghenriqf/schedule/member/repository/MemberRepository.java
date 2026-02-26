package com.ghenriqf.schedule.member.repository;

import com.ghenriqf.schedule.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Long countByMinistryId(Long id);
    Optional<Member> findByUserIdAndMinistryId(Long userId, Long ministryId);
    boolean existsByUserIdAndMinistryId(Long userId, Long ministryId);
    Optional<Member> findByIdAndMinistryId(Long memberId, Long ministryId);
    List<Member> findByMinistryId(Long ministryId);
}
