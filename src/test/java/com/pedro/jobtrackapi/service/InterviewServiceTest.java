package com.pedro.jobtrackapi.service;

import com.pedro.jobtrackapi.dto.interview.CreateInterviewRequest;
import com.pedro.jobtrackapi.dto.interview.InterviewResponse;
import com.pedro.jobtrackapi.dto.interview.UpdateInterviewRequest;
import com.pedro.jobtrackapi.enums.ApplicationStatus;
import com.pedro.jobtrackapi.enums.InterviewType;
import com.pedro.jobtrackapi.exception.BusinessException;
import com.pedro.jobtrackapi.exception.ResourceNotFoundException;
import com.pedro.jobtrackapi.model.Company;
import com.pedro.jobtrackapi.model.Interview;
import com.pedro.jobtrackapi.model.JobApplication;
import com.pedro.jobtrackapi.model.JobOpening;
import com.pedro.jobtrackapi.repository.InterviewRepository;
import com.pedro.jobtrackapi.repository.JobApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InterviewServiceTest {

    @Mock
    private InterviewRepository interviewRepository;
    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @InjectMocks
    private InterviewService interviewService;

    private JobApplication jobApplication;
    private Company company;
    private JobOpening jobOpening;
    private Interview interview;

    @BeforeEach
    public void setUp() {
        company = new Company();
        company.setId(1L);
        company.setName("Company Test");
        company.setLocation("Test");
        company.setWebsiteUrl("test.com");
        company.setLinkedinUrl("test.com");

        jobOpening = new JobOpening();
        jobOpening.setId(1L);
        jobOpening.setCompany(company);
        jobOpening.setTitle("Backend Intern");


        jobApplication = new JobApplication();
        jobApplication.setId(1L);
        jobApplication.setNotes("Test");
        jobApplication.setStatus(ApplicationStatus.APPLIED);
        jobApplication.setJobOpening(jobOpening);

        interview = new Interview();
        interview.setId(1L);
        interview.setJobApplication(jobApplication);
        interview.setScheduledAt(LocalDateTime.now().plusDays(1));
        interview.setType(InterviewType.TECHNICAL);
        interview.setInterviewer("Interviewer Test");
        interview.setNotes("Interview notes");
    }

    @Test
    void shouldCreateInterviewAndUpdateJobApplicationStatusToInterview(){
        CreateInterviewRequest request = new CreateInterviewRequest(
                jobApplication.getId(),
                interview.getScheduledAt(),
                interview.getType(),
                interview.getInterviewer(),
                interview.getNotes()
        );

        when(jobApplicationRepository.findById(jobApplication.getId())).thenReturn(Optional.of(jobApplication));

        when(interviewRepository.save(any(Interview.class))).thenAnswer(invocation -> {
            Interview saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        InterviewResponse response = interviewService.createInterview(request);

        assertNotNull(response);
        assertEquals(ApplicationStatus.INTERVIEW, jobApplication.getStatus());
        assertEquals(jobApplication.getId(), response.jobApplicationId());

        verify(jobApplicationRepository).save(jobApplication);
        verify(interviewRepository).save(any(Interview.class));
    }

    @Test
    void shouldThrowBusinessExceptionWhenInterviewIsScheduledInThePast(){
        CreateInterviewRequest request = new CreateInterviewRequest(
                jobApplication.getId(),
                LocalDateTime.now().minusDays(1),
                interview.getType(),
                interview.getInterviewer(),
                interview.getNotes()
        );

        when(jobApplicationRepository.findById(jobApplication.getId())).thenReturn(Optional.of(jobApplication));

        assertThrows(BusinessException.class, () -> {
            interviewService.createInterview(request);
        });

        verify(interviewRepository, never()).save(any(Interview.class));
        verify(jobApplicationRepository, never()).save(any(JobApplication.class));
    }

    @Test
    void shouldThrowBusinessExceptionWhenJobApplicationIsFinished(){
        jobApplication.setStatus(ApplicationStatus.REJECTED);
        CreateInterviewRequest request = new CreateInterviewRequest(
                jobApplication.getId(),
                LocalDateTime.now().plusDays(1),
                interview.getType(),
                interview.getInterviewer(),
                interview.getNotes()
        );

        when(jobApplicationRepository.findById(jobApplication.getId())).thenReturn(Optional.of(jobApplication));

        assertThrows(BusinessException.class, () -> {
            interviewService.createInterview(request);
        });

        verify(interviewRepository, never()).save(any(Interview.class));
        verify(jobApplicationRepository, never()).save(any(JobApplication.class));
    }
    @Test
    void shouldThrowBusinessExceptionWhenJobApplicationIsApproved(){
        jobApplication.setStatus(ApplicationStatus.APPROVED);
        CreateInterviewRequest request = new CreateInterviewRequest(
                jobApplication.getId(),
                LocalDateTime.now().plusDays(1),
                interview.getType(),
                interview.getInterviewer(),
                interview.getNotes()
        );

        when(jobApplicationRepository.findById(jobApplication.getId())).thenReturn(Optional.of(jobApplication));

        assertThrows(BusinessException.class, () -> {
            interviewService.createInterview(request);
        });

        verify(interviewRepository, never()).save(any(Interview.class));
        verify(jobApplicationRepository, never()).save(any(JobApplication.class));
    }

    @Test
    void shouldThrowResourceNotFoundWhenJobApplicationDoesNotExist(){
        Long nonExistingId = 999L;

        CreateInterviewRequest request = new CreateInterviewRequest(
                nonExistingId,
                LocalDateTime.now().plusDays(1),
                interview.getType(),
                interview.getInterviewer(),
                interview.getNotes()
        );

        when(jobApplicationRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            interviewService.createInterview(request);
        });

        verify(interviewRepository, never()).save(any(Interview.class));
        verify(jobApplicationRepository, never()).save(any(JobApplication.class));
    }

    @Test
    void shouldFindInterviewById(){
        when(interviewRepository.findById(interview.getId())).thenReturn(Optional.of(interview));

        InterviewResponse response = interviewService.findInterviewById(interview.getId());

        assertNotNull(response);
        assertEquals(interview.getId(), response.id());
        assertEquals(jobApplication.getId(), response.jobApplicationId());
        assertEquals(interview.getScheduledAt(), response.scheduledAt());
        assertEquals(interview.getType(), response.type());
        assertEquals(interview.getInterviewer(), response.interviewer());
        assertEquals(interview.getNotes(), response.notes());

        verify(interviewRepository).findById(interview.getId());
    }

    @Test
    void shouldThrowResourceNotFoundWhenInterviewDoesNotExist(){
        Long nonExistingId = 999L;

        when(interviewRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            interviewService.findInterviewById(nonExistingId);
        });

        verify(interviewRepository).findById(nonExistingId);
        verify(interviewRepository, never()).save(any(Interview.class));
    }

    @Test
    void shouldUpdateInterviewWhenValidData(){
        UpdateInterviewRequest request = new UpdateInterviewRequest(
           jobApplication.getId(),
           LocalDateTime.now().plusDays(1),
           interview.getType(),
           "Sr.Test",
           "New Notes"
        );

        when(interviewRepository.findById(interview.getId())).thenReturn(Optional.of(interview));
        when(jobApplicationRepository.findById(jobApplication.getId())).thenReturn(Optional.of(jobApplication));
        when(interviewRepository.save(any(Interview.class))).thenAnswer(invocation -> {
            Interview saved = invocation.getArgument(0);
            return saved;
        });

        InterviewResponse response = interviewService.updateInterview(interview.getId(), request);

        assertNotNull(response);
        assertEquals(interview.getId(), response.id());
        assertEquals(jobApplication.getId(), response.jobApplicationId());
        assertEquals(request.scheduledAt(), response.scheduledAt());
        assertEquals(request.type(), response.type());
        assertEquals(request.interviewer(), response.interviewer());
        assertEquals(request.notes(), response.notes());
    }

    @Test
    void shouldThrowBusinessExceptionWhenUpdatingInterviewToPastDate(){
        UpdateInterviewRequest request = new UpdateInterviewRequest(
                jobApplication.getId(),
                LocalDateTime.now().minusDays(2),
                interview.getType(),
                interview.getInterviewer(),
                interview.getNotes()
        );

        when(interviewRepository.findById(interview.getId())).thenReturn(Optional.of(interview));

        assertThrows(BusinessException.class, () -> {
            interviewService.updateInterview(interview.getId(), request);
        });

        verify(interviewRepository, never()).save(any(Interview.class));
    }

    @Test
    void shouldThrowResourceNotFoundWhenUpdatingNonExistingInterview(){
        Long nonExistingId = 999L;
        UpdateInterviewRequest request = new UpdateInterviewRequest(
                nonExistingId,
                LocalDateTime.now().plusDays(1),
                interview.getType(),
                interview.getInterviewer(),
                interview.getNotes()
        );

        when(interviewRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            interviewService.updateInterview(nonExistingId, request);
        });

        verify(interviewRepository).findById(nonExistingId);
        verify(interviewRepository, never()).save(any(Interview.class));
    }

    @Test
    void shouldDeleteInterviewWhenExists(){

        when(interviewRepository.findById(interview.getId())).thenReturn(Optional.of(interview));

        interviewService.deleteInterview(interview.getId());

        verify(interviewRepository).delete(interview);
    }

    @Test
    void shouldThrowResourceNotFoundWhenDeletingNonExistingInterview(){
        Long nonExistingId = 999L;

        when(interviewRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            interviewService.deleteInterview(nonExistingId);
        });

        verify(interviewRepository, never()).delete(any(Interview.class)    );
    }
}
