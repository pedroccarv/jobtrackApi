package com.pedro.jobtrackapi.dto.jobApplication;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateJobApplicationRequest(
        @NotNull
        Long jobOpeningId,
        String notes,
        LocalDate appliedAt
) {
}
