package com.pedro.jobtrackapi.service;

import com.pedro.jobtrackapi.dto.jobopening.CreateJobOpeningRequest;
import com.pedro.jobtrackapi.dto.jobopening.JobOpeningResponse;
import com.pedro.jobtrackapi.dto.jobopening.UpdateJobOpeningRequest;
import com.pedro.jobtrackapi.model.Company;
import com.pedro.jobtrackapi.model.JobOpening;
import com.pedro.jobtrackapi.repository.CompanyRepository;
import com.pedro.jobtrackapi.repository.JobOpeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobOpeningService {

    private final JobOpeningRepository jobOpeningRepository;
    private final CompanyRepository companyRepository;

    public JobOpeningResponse createJobOpening(CreateJobOpeningRequest createJobOpeningRequest) {
        JobOpening jobOpening = toEntity(createJobOpeningRequest);
        JobOpening savedJobOpening = jobOpeningRepository.save(jobOpening);
        return toResponse(savedJobOpening);
    }

    public JobOpeningResponse findJobOpeningById(Long id) {
        JobOpening jobOpening = findJobOpeningEntityById(id);
        return toResponse(jobOpening);
    }

    public List<JobOpeningResponse> findAll(){
        List<JobOpening> jobs = jobOpeningRepository.findAll();
        return jobs.stream().map(this::toResponse).toList();
    }

    public void deleteJobOpening(Long id){
        JobOpening jobOpening = findJobOpeningEntityById(id);
        jobOpeningRepository.delete(jobOpening);
    }

    public JobOpeningResponse updateJobOpening(Long id, UpdateJobOpeningRequest update) {
        JobOpening jobOpening = findJobOpeningEntityById(id);
        jobOpening.setJobUrl(update.jobUrl());
        jobOpening.setTitle(update.title());
        jobOpening.setLevel(update.level());
        jobOpening.setWorkMode(update.workMode());
            jobOpening.setCompany(findCompanyId(update.companyId()));
        jobOpening.setDescription(update.description());
        jobOpening.setPostedAt(update.postedAt());
        JobOpening savedJobOpening = jobOpeningRepository.save(jobOpening);
        return toResponse(savedJobOpening);
    }

    private JobOpening findJobOpeningEntityById(Long id) {
        return jobOpeningRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job opening not found"));
    }

    private JobOpeningResponse toResponse(JobOpening savedJobOpening) {
        return new  JobOpeningResponse(
                savedJobOpening.getId(),
                savedJobOpening.getTitle(),
                savedJobOpening.getDescription(),
                savedJobOpening.getLevel(),
                savedJobOpening.getWorkMode(),
                savedJobOpening.getJobUrl(),
                savedJobOpening.getPostedAt(),
                savedJobOpening.getCompany().getId(),
                savedJobOpening.getCompany().getName()
        );
    }


    private JobOpening toEntity(CreateJobOpeningRequest request ) {
        Company company = findCompanyId(request.companyId());
        JobOpening jobOpening = new JobOpening();
        jobOpening.setTitle(request.title());
        jobOpening.setJobUrl(request.jobUrl());
        jobOpening.setLevel(request.level());
        jobOpening.setCompany(company);
        jobOpening.setDescription(request.description());
        jobOpening.setPostedAt(request.postedAt());
        jobOpening.setWorkMode(request.workMode());
        return jobOpening;
    }

    private Company findCompanyId(Long id){
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }
}
