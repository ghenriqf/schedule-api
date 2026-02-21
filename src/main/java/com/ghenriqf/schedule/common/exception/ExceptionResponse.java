package com.ghenriqf.schedule.common.exception;

import lombok.Builder;

@Builder
public record ExceptionResponse(
        String code,
        String message,
        int status
) {
}
