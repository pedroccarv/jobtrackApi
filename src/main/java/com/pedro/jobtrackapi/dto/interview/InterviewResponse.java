package com.pedro.jobtrackapi.dto.interview;

import com.pedro.jobtrackapi.enums.InterviewType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record InterviewResponse (
        Long id,
        Long jobApplicationId,
        String jobOpeningTitle,
        String companyName,
        LocalDateTime scheduledAt,
        InterviewType type,
        String interviewer,
        String notes
){
}
