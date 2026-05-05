package com.pedro.jobtrackapi.service;

import com.pedro.jobtrackapi.dto.jobopening.CreateJobOpeningRequest;
import com.pedro.jobtrackapi.dto.jobopening.JobOpeningResponse;
import com.pedro.jobtrackapi.dto.jobopening.UpdateJobOpeningRequest;
import com.pedro.jobtrackapi.dto.technology.TechnologyResponse;
import com.pedro.jobtrackapi.exception.ResourceNotFoundException;
import com.pedro.jobtrackapi.model.Company;
import com.pedro.jobtrackapi.model.JobOpening;
import com.pedro.jobtrackapi.model.Technology;
import com.pedro.jobtrackapi.repository.CompanyRepository;
import com.pedro.jobtrackapi.repository.JobOpeningRepository;
import com.pedro.jobtrackapi.repository.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobOpeningService {

    private final JobOpeningRepository jobOpeningRepository;
    private final CompanyRepository companyRepository;
    private final TechnologyRepository technologyRepository;

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
            jobOpening.setCompany(findCompanyById(update.companyId()));
        jobOpening.setDescription(update.description());
        jobOpening.setPostedAt(update.postedAt());
        jobOpening.setTechnologies(findTechnologiesByIds(update.technologyIds()));
        JobOpening savedJobOpening = jobOpeningRepository.save(jobOpening);
        return toResponse(savedJobOpening);
    }

    private JobOpening findJobOpeningEntityById(Long id) {
        return jobOpeningRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException( "Job opening not found"));
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
                savedJobOpening.getCompany().getName(),
                savedJobOpening.getTechnologies()
                        .stream()
                        .map(technology ->
                                new TechnologyResponse(technology.getId(), technology.getName()))
                        .collect(Collectors.toSet())
        );
    }


    private JobOpening toEntity(CreateJobOpeningRequest request ) {
        Company company = findCompanyById(request.companyId());
        Set<Technology> technologies = findTechnologiesByIds(request.technologyIds());

        JobOpening jobOpening = new JobOpening();
        jobOpening.setTitle(request.title());
        jobOpening.setJobUrl(request.jobUrl());
        jobOpening.setLevel(request.level());
        jobOpening.setCompany(company);
        jobOpening.setDescription(request.description());
        jobOpening.setPostedAt(request.postedAt());
        jobOpening.setWorkMode(request.workMode());
        jobOpening.setTechnologies(technologies);
        return jobOpening;
    }

    private Company findCompanyById(Long id){
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
    }

    private Set<Technology> findTechnologiesByIds(Set<Long> technologyIds){
        if (technologyIds == null){
            return new HashSet<>();
        }

        List<Technology> technologies  = technologyRepository.findAllById(technologyIds);

        if (technologies .size() != technologyIds.size()){
            throw new ResourceNotFoundException("One or more technologies were not found");
        }
        return new HashSet<>(technologies);
    }
}
