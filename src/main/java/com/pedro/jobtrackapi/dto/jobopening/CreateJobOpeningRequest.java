package com.pedro.jobtrackapi.dto.jobopening;

import com.pedro.jobtrackapi.enums.WorkMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateJobOpeningRequest(
        @NotBlank
        String title,
        String description,
        String level,
        @NotNull
        WorkMode workMode,
        String jobUrl,
        LocalDate postedAt,
        @NotNull
        Long companyId

) {
}
