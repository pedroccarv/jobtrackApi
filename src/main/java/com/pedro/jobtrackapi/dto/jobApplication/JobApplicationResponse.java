package com.pedro.jobtrackapi.dto.jobApplication;

import com.pedro.jobtrackapi.enums.ApplicationStatus;

import java.time.LocalDate;

public record JobApplicationResponse(
        Long id,
        Long jobOpeningId,
        String jobOpeningTitle,
        String companyName,
        ApplicationStatus status,
        String notes,
        LocalDate appliedAt
) {
}
