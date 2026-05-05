package com.pedro.jobtrackapi.service;

import com.pedro.jobtrackapi.dto.interview.CreateInterviewRequest;
import com.pedro.jobtrackapi.dto.interview.InterviewResponse;
import com.pedro.jobtrackapi.dto.interview.UpdateInterviewRequest;
import com.pedro.jobtrackapi.enums.ApplicationStatus;
import com.pedro.jobtrackapi.exception.BusinessException;
import com.pedro.jobtrackapi.exception.ResourceNotFoundException;
import com.pedro.jobtrackapi.model.Interview;
import com.pedro.jobtrackapi.model.JobApplication;
import com.pedro.jobtrackapi.repository.InterviewRepository;
import com.pedro.jobtrackapi.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public InterviewResponse findInterviewById(Long interviewId) {
        Interview interviewEntity = getInterviewEntity(interviewId);
        return toResponse(interviewEntity);
    }

    public List<InterviewResponse> findAll() {
        List<Interview> interviews = interviewRepository.findAll();
        return interviews.stream().map(this::toResponse).toList();
    }

    public InterviewResponse updateInterview(Long interviewId, UpdateInterviewRequest request) {
        Interview interviewEntity = getInterviewEntity(interviewId);

        if (request.scheduledAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Interview cannot be scheduled in the past");
        }

        JobApplication jobApplication = findJobApplicationEntity(request.jobApplicationId());

        if (jobApplication.getStatus().equals(ApplicationStatus.APPROVED) || jobApplication.getStatus().equals(ApplicationStatus.REJECTED)) {
            throw new BusinessException("Cannot schedule interview for a finished application");
        }

        interviewEntity.setType(request.type());
        interviewEntity.setNotes(request.notes());
        interviewEntity.setScheduledAt(request.scheduledAt());
        interviewEntity.setJobApplication(jobApplication);
        interviewEntity.setInterviewer(request.interviewer());
        Interview interviewSaved = interviewRepository.save(interviewEntity);
        return toResponse(interviewSaved);
    }

    public InterviewResponse createInterview(CreateInterviewRequest request) {
        JobApplication jobApplication = findJobApplicationEntity(request.jobApplicationId());

        if (jobApplication.getStatus().equals(ApplicationStatus.APPROVED) || jobApplication.getStatus().equals(ApplicationStatus.REJECTED)) {
            throw new BusinessException("Cannot schedule interview for a finished application");
        }

        if (request.scheduledAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Interview cannot be scheduled in the past");
        }

        Interview interviewEntity = toEntity(request, jobApplication);
        Interview interviewSaved = interviewRepository.save(interviewEntity);

        jobApplication.setStatus(ApplicationStatus.INTERVIEW);
        jobApplicationRepository.save(jobApplication);
        return toResponse(interviewSaved);
    }

    public void deleteInterview(Long id){
        Interview interviewEntity = getInterviewEntity(id);
        interviewRepository.delete(interviewEntity);
    }

    private JobApplication findJobApplicationEntity(Long jobApplicationId) {
        return jobApplicationRepository.findById(jobApplicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Job Application not found"));
    }

    private Interview getInterviewEntity(Long id){
        return  interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found"));
    }

    private InterviewResponse toResponse(Interview interview){
        return new InterviewResponse(
                interview.getId(),
                interview.getJobApplication().getId(),
                interview.getJobApplication().getJobOpening().getTitle(),
                interview.getJobApplication().getJobOpening().getCompany().getName(),
                interview.getScheduledAt(),
                interview.getType(),
                interview.getInterviewer(),
                interview.getNotes()
        );
    }

    private Interview toEntity(CreateInterviewRequest createInterviewRequest, JobApplication jobApplication) {
        Interview interview = new Interview();

        interview.setJobApplication(jobApplication);
        interview.setScheduledAt(createInterviewRequest.scheduledAt());
        interview.setNotes(createInterviewRequest.notes());
        interview.setType(createInterviewRequest.type());
        interview.setInterviewer(createInterviewRequest.interviewer());
        return interview;
    }
}
