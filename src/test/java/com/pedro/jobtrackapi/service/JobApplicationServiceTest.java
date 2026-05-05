package com.pedro.jobtrackapi.service;

import com.pedro.jobtrackapi.dto.jobapplication.CreateJobApplicationRequest;
import com.pedro.jobtrackapi.dto.jobapplication.JobApplicationResponse;
import com.pedro.jobtrackapi.enums.ApplicationStatus;
import com.pedro.jobtrackapi.enums.WorkMode;
import com.pedro.jobtrackapi.exception.BusinessException;
import com.pedro.jobtrackapi.exception.ResourceNotFoundException;
import com.pedro.jobtrackapi.model.Company;
import com.pedro.jobtrackapi.model.JobApplication;
import com.pedro.jobtrackapi.model.JobOpening;
import com.pedro.jobtrackapi.repository.JobApplicationRepository;
import com.pedro.jobtrackapi.repository.JobOpeningRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class JobApplicationServiceTest {

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @Mock
    private JobOpeningRepository jobOpeningRepository;

    @InjectMocks
    private JobApplicationService jobApplicationService;

    private Company company;
    private JobOpening jobOpening;

    @BeforeEach
    void setUp(){
        company = new Company();
        company.setId(1L);
        company.setName("Company Name");

        jobOpening = new JobOpening();
        jobOpening.setId(1L);
        jobOpening.setTitle("Backend Intern");
        jobOpening.setWorkMode(WorkMode.HYBRID);
        jobOpening.setCompany(company);

    }

    @Test
    void shouldCreateJobApplicationWithAppliedStatus(){
        CreateJobApplicationRequest request = new CreateJobApplicationRequest(
                jobOpening.getId(),
                "Test",
                LocalDate.now()
        );

        when(jobOpeningRepository.findById(jobOpening.getId())).thenReturn(Optional.of(jobOpening));
        when(jobApplicationRepository.existsByJobOpening(jobOpening)).thenReturn(false);

        when(jobApplicationRepository.save(any(JobApplication.class))).thenAnswer(invocation -> {
                JobApplication saved = invocation.getArgument(0);
                saved.setId(1L);
                return saved;
                });

        JobApplicationResponse response = jobApplicationService.createJobApplication(request);

        assertNotNull(response);
        assertEquals(ApplicationStatus.APPLIED, response.status());
        assertEquals(jobOpening.getId(), response.jobOpeningId());
    }

    @Test
    void shouldThrowResourceNotFoundWhenJobOpeningDoesNotExist(){
        Long nonExistingId = 999L;

        CreateJobApplicationRequest request = new CreateJobApplicationRequest(
                nonExistingId,
                "Test",
                LocalDate.now()
        );
        when(jobOpeningRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            jobApplicationService.createJobApplication(request);
        });

        verify(jobApplicationRepository, never()).save(any(JobApplication.class));
    }

    @Test
    void shouldThrowBusinessExceptionWhenJobApplicationAlreadyExistsForJobOpening(){
        Long existingId = 1L;

        CreateJobApplicationRequest request = new CreateJobApplicationRequest(
                existingId,
                "Test",
                LocalDate.now()
        );
        when(jobOpeningRepository.findById(existingId)).thenReturn(Optional.of(jobOpening));
        when(jobApplicationRepository.existsByJobOpening(jobOpening)).thenReturn(true);

        assertThrows(BusinessException.class, () -> {
           jobApplicationService.createJobApplication(request);
        });

        verify(jobApplicationRepository, never()).save(any(JobApplication.class));
    }

    @Test
    void shouldUseCurrentDateWhenAppliedAtIsNull(){
        CreateJobApplicationRequest request = new CreateJobApplicationRequest(
                jobOpening.getId(),
                "Test",
                null
        );
        when(jobOpeningRepository.findById(jobOpening.getId())).thenReturn(Optional.of(jobOpening));
        when(jobApplicationRepository.existsByJobOpening(jobOpening)).thenReturn(false);
        when(jobApplicationRepository.save(any(JobApplication.class))).thenAnswer(invocation ->
                {
                JobApplication saved = invocation.getArgument(0);
                return saved;
                });
        JobApplicationResponse response = jobApplicationService.createJobApplication(request);

        assertEquals(LocalDate.now(), response.appliedAt());
        assertEquals(ApplicationStatus.APPLIED, response.status());

        verify(jobApplicationRepository).save(any(JobApplication.class));
    }

    @Test
    void shouldFindJobApplicationById(){
        Long existingId = 1L;
        JobApplication jobApplication = new JobApplication();
        jobApplication.setId(existingId);
        jobApplication.setStatus(ApplicationStatus.IN_REVIEW);
        jobApplication.setNotes("Test");
        jobApplication.setAppliedAt(LocalDate.now());
        jobApplication.setJobOpening(jobOpening);

        when(jobApplicationRepository.findById(existingId)).thenReturn(Optional.of(jobApplication));

        JobApplicationResponse response = jobApplicationService.findById(existingId);

        assertEquals(existingId, response.id());
        assertEquals(ApplicationStatus.IN_REVIEW, response.status());
        assertEquals(jobOpening.getId(), response.jobOpeningId());
        assertEquals(jobOpening.getTitle(), response.jobOpeningTitle());
    }

    @Test
    void shouldThrowResourceNotFoundWhenJobApplicationDoesNotExist(){
        Long nonExistingId = 999L;


        when(jobApplicationRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            jobApplicationService.findById(nonExistingId);
        });

        verify(jobApplicationRepository, never()).save(any(JobApplication.class));
    }

    @Test
    void shouldDeleteJobApplicationWhenExists(){
        Long existingId = 1L;
        JobApplication jobApplication = new JobApplication();
        jobApplication.setId(existingId);
        jobApplication.setStatus(ApplicationStatus.IN_REVIEW);
        jobApplication.setNotes("Test");
        jobApplication.setAppliedAt(LocalDate.now());
        jobApplication.setJobOpening(jobOpening);

        when(jobApplicationRepository.findById(existingId)).thenReturn(Optional.of(jobApplication));

        jobApplicationService.deleteJobApplication(existingId);

        verify(jobApplicationRepository).delete(jobApplication);
    }

    @Test
    void shouldThrowResourceNotFoundWhenDeletingNonExistingJobApplication(){
        Long nonExistingId = 999L;

        when(jobApplicationRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            jobApplicationService.deleteJobApplication(nonExistingId);
        });

        verify(jobApplicationRepository, never()).delete(any(JobApplication.class));
    }
}
