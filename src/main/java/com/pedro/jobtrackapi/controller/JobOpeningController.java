package com.pedro.jobtrackapi.controller;

import com.pedro.jobtrackapi.dto.jobopening.CreateJobOpeningRequest;
import com.pedro.jobtrackapi.dto.jobopening.JobOpeningResponse;
import com.pedro.jobtrackapi.dto.jobopening.UpdateJobOpeningRequest;
import com.pedro.jobtrackapi.service.JobOpeningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job-openings")
@RequiredArgsConstructor
public class JobOpeningController {

    private final JobOpeningService jobOpeningService;

    @GetMapping
    public ResponseEntity<List<JobOpeningResponse>> getAllJobOpenings() {
        List<JobOpeningResponse> allJobOpening = jobOpeningService.findAll();
        return ResponseEntity.ok(allJobOpening);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobOpeningResponse> getJobOpening(@PathVariable Long id) {
        JobOpeningResponse jobOpeningById = jobOpeningService.findJobOpeningById(id);
        return ResponseEntity.ok(jobOpeningById);
    }

    @PostMapping
    public ResponseEntity<JobOpeningResponse> createJobOpening(@Valid @RequestBody CreateJobOpeningRequest createJobOpeningRequest) {
        JobOpeningResponse jobOpening = jobOpeningService.createJobOpening(createJobOpeningRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobOpening);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobOpeningResponse> updateJobOpening(@PathVariable Long id, @Valid @RequestBody UpdateJobOpeningRequest updateJobOpening) {
        JobOpeningResponse jobOpeningResponse = jobOpeningService.updateJobOpening(id, updateJobOpening);
        return  ResponseEntity.ok(jobOpeningResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobOpening(@PathVariable Long id) {
        jobOpeningService.deleteJobOpening(id);
        return ResponseEntity.noContent().build();
    }
}
