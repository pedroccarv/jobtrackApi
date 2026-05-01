package com.pedro.jobtrackapi.service;

import com.pedro.jobtrackapi.dto.company.CompanyResponse;
import com.pedro.jobtrackapi.dto.company.CreateCompanyRequest;
import com.pedro.jobtrackapi.model.Company;
import com.pedro.jobtrackapi.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyResponse createCompany(CreateCompanyRequest companyRequest) {
        Company company = toEntity(companyRequest);
        Company savedCompany = companyRepository.save(company);
        return toResponse(savedCompany);
    }

    public List<CompanyResponse> findAll() {
        List<Company> allCompanies = companyRepository.findAll();
        return allCompanies.stream().map(this::toResponse).toList();
    }

    private Company toEntity(CreateCompanyRequest companyRequest) {
        Company company = new Company();
        company.setName(companyRequest.name());
        company.setLocation(companyRequest.location());
        company.setNotes(companyRequest.notes());
        company.setLinkedinUrl(companyRequest.linkedinUrl());
        company.setWebsiteUrl(companyRequest.websiteUrl());
        return company;
    }

    private CompanyResponse toResponse(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getLinkedinUrl(),
                company.getWebsiteUrl(),
                company.getLocation(),
                company.getNotes());
    }
}
