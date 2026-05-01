package com.pedro.jobtrackapi.controller;

import com.pedro.jobtrackapi.dto.company.CompanyResponse;
import com.pedro.jobtrackapi.dto.company.CreateCompanyRequest;
import com.pedro.jobtrackapi.dto.company.UpdateCompanyRequest;
import com.pedro.jobtrackapi.repository.CompanyRepository;
import com.pedro.jobtrackapi.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyRepository companyRepository;

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@Valid @RequestBody CreateCompanyRequest dto){
        CompanyResponse company = companyService.createCompany(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> allCompanies() {
        List<CompanyResponse> companyResponses = companyService.findAll();
        return ResponseEntity.ok(companyResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompany(@PathVariable Long id){
        CompanyResponse company = companyService.findCompanyById(id);
        return ResponseEntity.ok(company);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable Long id, @Valid @RequestBody UpdateCompanyRequest dto){
        CompanyResponse company = companyService.updateCompany(id, dto);
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id){
        companyRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
