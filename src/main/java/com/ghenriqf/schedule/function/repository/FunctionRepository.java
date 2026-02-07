package com.ghenriqf.schedule.function.repository;

import com.ghenriqf.schedule.function.entity.Function;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FunctionRepository extends JpaRepository<Function, Long> {
}
