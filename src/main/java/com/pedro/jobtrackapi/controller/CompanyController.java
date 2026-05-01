package com.pedro.jobtrackapi.controller;

import com.pedro.jobtrackapi.dto.company.CompanyResponse;
import com.pedro.jobtrackapi.dto.company.CreateCompanyRequest;
import com.pedro.jobtrackapi.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
