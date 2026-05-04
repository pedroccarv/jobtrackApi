package com.pedro.jobtrackapi.dto.jobApplication;

import com.pedro.jobtrackapi.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateJobApplicationRequest(
        @NotNull
        Long jobOpeningId,
        @NotNull
        ApplicationStatus status,
        String notes,
        @NotNull
        LocalDate appliedAt
) {
}
