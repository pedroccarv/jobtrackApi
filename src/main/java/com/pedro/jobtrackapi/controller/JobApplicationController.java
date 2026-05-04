package com.pedro.jobtrackapi.controller;

import com.pedro.jobtrackapi.dto.jobapplication.CreateJobApplicationRequest;
import com.pedro.jobtrackapi.dto.jobapplication.JobApplicationResponse;
import com.pedro.jobtrackapi.dto.jobapplication.UpdateJobApplicationRequest;
import com.pedro.jobtrackapi.model.JobApplication;
import com.pedro.jobtrackapi.service.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job-applications")
@RequiredArgsConstructor
public class JobApplicationController {
    
    private final JobApplicationService jobApplicationService;
    
    @GetMapping
    public ResponseEntity<List<JobApplicationResponse>> getAllJobApplications() {
        List<JobApplicationResponse> allJobs = jobApplicationService.findAll();
        return ResponseEntity.ok(allJobs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> getJobApplicationById(@PathVariable Long id) {
        JobApplicationResponse jobApplication = jobApplicationService.findById(id);
        return ResponseEntity.ok(jobApplication);
    }

    @PostMapping
    public ResponseEntity<JobApplicationResponse> createJobApplication(@Valid @RequestBody CreateJobApplicationRequest createJobApplicationRequest) {
        JobApplicationResponse jobApplication = jobApplicationService.createJobApplication(createJobApplicationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobApplication);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> updateJobApplication(@PathVariable Long id, @Valid @RequestBody UpdateJobApplicationRequest updateJobApplicationRequest) {
        JobApplicationResponse jobApplicationResponse = jobApplicationService.updateJobApplication(id, updateJobApplicationRequest);
        return ResponseEntity.ok(jobApplicationResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobApplication(@PathVariable Long id) {
        jobApplicationService.deleteJobApplication(id);
        return ResponseEntity.noContent().build();
    }
}
