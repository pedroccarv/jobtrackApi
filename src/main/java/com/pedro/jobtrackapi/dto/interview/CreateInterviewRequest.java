package com.pedro.jobtrackapi.dto.interview;

import com.pedro.jobtrackapi.enums.InterviewType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateInterviewRequest(
        @NotNull
        Long jobApplicationId,

        @NotNull
        @Future
        LocalDateTime scheduledAt,
        @NotNull
        InterviewType type,
        String interviewer,
        String notes

) {
}
