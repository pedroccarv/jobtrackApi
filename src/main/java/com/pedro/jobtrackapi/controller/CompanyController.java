package com.pedro.jobtrackapi.controller;

import com.pedro.jobtrackapi.dto.company.CompanyResponse;
import com.pedro.jobtrackapi.dto.company.CreateCompanyRequest;
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
}
