package com.ghenriqf.schedule.member.repository;

import com.ghenriqf.schedule.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
