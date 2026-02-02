package com.ghenriqf.schedule.common.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record PageRequestDTO(
        @Min(0) Integer page,
        @Min(1) @Max(50) Integer size
) {
    public Pageable toPageable() {
        int pageValue = page == null ? 0 : page;
        int sizeValue = size == null ? 10 : size;
        return PageRequest.of(pageValue, sizeValue);
    }
}