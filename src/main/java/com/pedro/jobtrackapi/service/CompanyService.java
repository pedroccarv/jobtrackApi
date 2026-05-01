package com.pedro.jobtrackapi.service;

import com.pedro.jobtrackapi.dto.company.CompanyResponse;
import com.pedro.jobtrackapi.dto.company.CreateCompanyRequest;
import com.pedro.jobtrackapi.dto.company.UpdateCompanyRequest;
import com.pedro.jobtrackapi.model.Company;
import com.pedro.jobtrackapi.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public CompanyResponse findCompanyById(Long id){
        Company company = findCompanyEntityById(id);
        return toResponse(company);
    }

    public CompanyResponse updateCompany(Long id, UpdateCompanyRequest companyUpdate) {
        Company company = findCompanyEntityById(id);

        company.setName(companyUpdate.name());
        company.setLinkedinUrl(companyUpdate.linkedinUrl());
        company.setWebsiteUrl(companyUpdate.websiteUrl());
        company.setNotes(companyUpdate.notes());
        company.setLocation(companyUpdate.location());

        Company updatedCompany = companyRepository.save(company);

        return toResponse(updatedCompany);
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

    private Company findCompanyEntityById(Long id){
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }
}
