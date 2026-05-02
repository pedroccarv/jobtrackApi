package com.pedro.jobtrackapi.dto.technology;

import jakarta.validation.constraints.NotBlank;

public record CreateTechnologyRequest (
        @NotBlank
        String name
){
}
