package com.ghenriqf.schedule.common.exception;

import lombok.Builder;

@Builder
public record ExceptionReponse(
        String code,
        String message,
        int status
) {
}
