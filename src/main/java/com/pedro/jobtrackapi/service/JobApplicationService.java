package com.pedro.jobtrackapi.service;

import com.pedro.jobtrackapi.dto.jobapplication.CreateJobApplicationRequest;
import com.pedro.jobtrackapi.dto.jobapplication.JobApplicationResponse;
import com.pedro.jobtrackapi.dto.jobapplication.UpdateJobApplicationRequest;
import com.pedro.jobtrackapi.enums.ApplicationStatus;
import com.pedro.jobtrackapi.exception.BusinessException;
import com.pedro.jobtrackapi.exception.ResourceNotFoundException;
import com.pedro.jobtrackapi.model.JobApplication;
import com.pedro.jobtrackapi.model.JobOpening;
import com.pedro.jobtrackapi.repository.JobApplicationRepository;
import com.pedro.jobtrackapi.repository.JobOpeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobOpeningRepository jobOpeningRepository;

    public JobApplicationResponse findById(Long id) {
        JobApplication jobApplicationEntity = findJobApplicationEntity(id);
        return toResponse(jobApplicationEntity);
    }

    public List<JobApplicationResponse> findAll() {
        List<JobApplication> jobApplications = jobApplicationRepository.findAll();
        return jobApplications.stream().map(this::toResponse).toList();
    }

    public JobApplicationResponse createJobApplication(CreateJobApplicationRequest request) {
        JobOpening jobOpening = findJobOpeningById(request.jobOpeningId());
        if (jobApplicationRepository.existsByJobOpening(jobOpening)){
            throw new BusinessException("Job already exists");
        };
        JobApplication jobApplication = toEntity(request, jobOpening);
        JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);

        return toResponse(savedJobApplication);
    }

    public JobApplicationResponse updateJobApplication(Long id, UpdateJobApplicationRequest updateJobApplication) {
        JobApplication jobApplicationEntity = findJobApplicationEntity(id);
        jobApplicationEntity.setJobOpening(findJobOpeningById(updateJobApplication.jobOpeningId()));
        jobApplicationEntity.setNotes(updateJobApplication.notes());
        jobApplicationEntity.setStatus(updateJobApplication.status());
        jobApplicationEntity.setAppliedAt(updateJobApplication.appliedAt());

        JobApplication jobApplicationSaved = jobApplicationRepository.save(jobApplicationEntity);
        return toResponse(jobApplicationSaved);
    }

    public void deleteJobApplication(Long id) {
        JobApplication jobApplicationEntity = findJobApplicationEntity(id);
        jobApplicationRepository.delete(jobApplicationEntity);
    }

    private JobApplication findJobApplicationEntity(Long id){
        return jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException( "Job Application Not Found"));
    }

    private JobApplicationResponse toResponse(JobApplication jobApplication) {
        return new JobApplicationResponse(
                jobApplication.getId(),
                jobApplication.getJobOpening().getId(),
                jobApplication.getJobOpening().getTitle(),
                jobApplication.getJobOpening().getCompany().getName(),
                jobApplication.getStatus(),
                jobApplication.getNotes(),
                jobApplication.getAppliedAt()
        );
    }

    private JobApplication toEntity(CreateJobApplicationRequest request, JobOpening jobOpening) {
        JobApplication jobApplication = new JobApplication();

        jobApplication.setJobOpening(jobOpening);
        jobApplication.setStatus(ApplicationStatus.APPLIED);
        jobApplication.setNotes(request.notes());
        jobApplication.setAppliedAt(
                request.appliedAt() != null ? request.appliedAt() : LocalDate.now()
        );
        return jobApplication;
    }

    private JobOpening findJobOpeningById(Long id){
        return jobOpeningRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job Opening Not Found"));
    }
}
