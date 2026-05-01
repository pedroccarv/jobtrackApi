package com.pedro.jobtrackapi.dto.company;

public record CompanyResponse (
        Long id,
        String name,
        String linkedinUrl,
        String websiteUrl,
        String location,
        String notes
){}
