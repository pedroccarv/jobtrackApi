package com.pedro.jobtrackapi.dto.technology;

import jakarta.validation.constraints.NotBlank;

public record UpdateTechnologyRequest(
        @NotBlank
        String name
) {
}
