package com.pedro.jobtrackapi.dto.company;

import jakarta.validation.constraints.NotBlank;

public record UpdateCompanyRequest(
        @NotBlank
        String name,
        String linkedinUrl,
        String websiteUrl,
        String location,
        String notes
) {
}
