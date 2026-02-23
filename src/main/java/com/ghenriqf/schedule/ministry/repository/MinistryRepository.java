package com.ghenriqf.schedule.ministry.repository;

import com.ghenriqf.schedule.ministry.dto.response.MinistryResponse;
import com.ghenriqf.schedule.ministry.entity.Ministry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MinistryRepository extends JpaRepository<Ministry, Long> {

    @Query("""
        SELECT DISTINCT new com.ghenriqf.schedule.ministry.dto.response.MinistryResponse(
            m.ministry.id,
            m.ministry.name,
            m.ministry.description,
            m.ministry.avatarUrl
        )
        FROM Member m
        WHERE m.user.id = :userId
    """)
    List<MinistryResponse> findMinistriesByUserId(Long userId);
    Optional<Ministry> findByInviteCode(String inviteCode);
}