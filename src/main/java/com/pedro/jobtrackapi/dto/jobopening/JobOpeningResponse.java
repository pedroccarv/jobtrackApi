package com.pedro.jobtrackapi.dto.jobopening;

import com.pedro.jobtrackapi.dto.technology.TechnologyResponse;
import com.pedro.jobtrackapi.enums.WorkMode;

import java.time.LocalDate;
import java.util.Set;

public record JobOpeningResponse(
        Long id,
        String title,
        String description,
        String level,
        WorkMode workMode,
        String jobUrl,
        LocalDate postedAt,
        Long companyId,
        String companyName,
        Set<TechnologyResponse> technologies
) {
}
