package com.pedro.jobtrackapi.controller;

import com.pedro.jobtrackapi.dto.interview.CreateInterviewRequest;
import com.pedro.jobtrackapi.dto.interview.InterviewResponse;
import com.pedro.jobtrackapi.dto.interview.UpdateInterviewRequest;
import com.pedro.jobtrackapi.service.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @GetMapping
    public ResponseEntity<List<InterviewResponse>> findAllInterviews() {
        List<InterviewResponse> allInterviews = interviewService.findAll();
        return ResponseEntity.ok(allInterviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewResponse> findInterviewById(@PathVariable Long id) {
        InterviewResponse interviewById = interviewService.findInterviewById(id);
        return ResponseEntity.ok(interviewById);
    }

    @PostMapping
    public ResponseEntity<InterviewResponse> createInterview(@Valid @RequestBody CreateInterviewRequest createInterviewRequest){
        InterviewResponse interviewResponse = interviewService.createInterview(createInterviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(interviewResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterviewResponse> updateInterview(@PathVariable Long id, @Valid @RequestBody UpdateInterviewRequest updateInterviewRequest){
        InterviewResponse interviewResponse = interviewService.updateInterview(id, updateInterviewRequest);
        return ResponseEntity.ok(interviewResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterview(@PathVariable Long id){
        interviewService.deleteInterview(id);
        return ResponseEntity.noContent().build();
    }

}
