package com.pedro.jobtrackapi.dto.jobopening;

import com.pedro.jobtrackapi.enums.WorkMode;

import java.time.LocalDate;

public record JobOpeningResponse(
        Long id,
        String title,
        String description,
        String level,
        WorkMode workMode,
        String jobUrl,
        LocalDate postedAt,
        Long companyId,
        String companyName
) {
}
