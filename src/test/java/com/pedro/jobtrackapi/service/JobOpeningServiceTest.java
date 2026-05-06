package com.pedro.jobtrackapi.service;

import com.pedro.jobtrackapi.dto.jobopening.CreateJobOpeningRequest;
import com.pedro.jobtrackapi.dto.jobopening.JobOpeningResponse;
import com.pedro.jobtrackapi.dto.jobopening.UpdateJobOpeningRequest;
import com.pedro.jobtrackapi.enums.WorkMode;
import com.pedro.jobtrackapi.exception.ResourceNotFoundException;
import com.pedro.jobtrackapi.model.Company;
import com.pedro.jobtrackapi.model.JobOpening;
import com.pedro.jobtrackapi.model.Technology;
import com.pedro.jobtrackapi.repository.CompanyRepository;
import com.pedro.jobtrackapi.repository.JobOpeningRepository;
import com.pedro.jobtrackapi.repository.TechnologyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobOpeningServiceTest {
    @Mock
    private JobOpeningRepository jobOpeningRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private TechnologyRepository technologyRepository;

    @InjectMocks
    private JobOpeningService jobOpeningService;

    private JobOpening jobOpening;
    private Company company;
    private Set<Technology> technologies = new HashSet<>();

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(1L);
        company.setName("Company Name");

        Technology tech1 = new Technology();
        tech1.setId(1L);
        tech1.setName("Technology Name");
        technologies.add(tech1);

        Technology tech2 = new Technology();
        tech2.setId(2L);
        tech2.setName("Technology test");
        technologies.add(tech2);

        jobOpening = new JobOpening();
        jobOpening.setId(1L);
        jobOpening.setTitle("Backend Intern");
        jobOpening.setWorkMode(WorkMode.HYBRID);
        jobOpening.setCompany(company);
        jobOpening.setTechnologies(technologies);
        jobOpening.setDescription("Backend job");
        jobOpening.setJobUrl("test.com");
        jobOpening.setLevel("Junior");
        jobOpening.setPostedAt(LocalDate.now());
    }

    @Test
    void shouldCreateJobOpeningSuccessfully(){
        CreateJobOpeningRequest request = new CreateJobOpeningRequest(
                jobOpening.getTitle(),
                jobOpening.getDescription(),
                jobOpening.getLevel(),
                jobOpening.getWorkMode(),
                jobOpening.getJobUrl(),
                jobOpening.getPostedAt(),
                jobOpening.getCompany().getId(),
                jobOpening.getTechnologies().stream().map(Technology::getId).collect(Collectors.toSet())
        );

        when(companyRepository.findById(request.companyId())).thenReturn(Optional.of(company));
        when(technologyRepository.findAllById(request.technologyIds())).thenReturn(new ArrayList<>(technologies));
        when(jobOpeningRepository.save(any(JobOpening.class))).thenAnswer(invocation -> invocation.getArgument(0));

        JobOpeningResponse response = jobOpeningService.createJobOpening(request);

        assertNotNull(response);
        assertEquals(jobOpening.getTitle(), response.title());
        assertEquals(company.getId(), response.companyId());
        assertEquals(company.getName(), response.companyName());
        assertEquals(technologies.size(), response.technologies().size());
        assertEquals(jobOpening.getDescription(), response.description());
        assertEquals(jobOpening.getLevel(), response.level());

        verify(companyRepository).findById(company.getId());
        verify(technologyRepository).findAllById(request.technologyIds());
        verify(jobOpeningRepository).save(any(JobOpening.class));
    }

    @Test
    void shouldThrowResourceNotFoundWhenCompanyDoesNotExist(){
        Long nonExistingId = 999L;
        CreateJobOpeningRequest request = new CreateJobOpeningRequest(
                jobOpening.getTitle(),
                jobOpening.getDescription(),
                jobOpening.getLevel(),
                jobOpening.getWorkMode(),
                jobOpening.getJobUrl(),
                jobOpening.getPostedAt(),
                nonExistingId,
                jobOpening.getTechnologies().stream().map(Technology::getId).collect(Collectors.toSet())
        );

        when(companyRepository.findById(request.companyId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
           jobOpeningService.createJobOpening(request);
        });

        verify(jobOpeningRepository, never()).save(any(JobOpening.class));
        verify(companyRepository).findById(nonExistingId);
        verify(technologyRepository, never()).findAllById(any());
    }

    @Test
    void shouldThrowResourceNotFoundWhenTechnologyIdsAreInvalid(){
        Set<Long> invalidIds = Set.of(999L, 888L);
        CreateJobOpeningRequest request = new CreateJobOpeningRequest(
                jobOpening.getTitle(),
                jobOpening.getDescription(),
                jobOpening.getLevel(),
                jobOpening.getWorkMode(),
                jobOpening.getJobUrl(),
                jobOpening.getPostedAt(),
                jobOpening.getCompany().getId(),
                invalidIds
        );
        when(companyRepository.findById(jobOpening.getCompany().getId())).thenReturn(Optional.of(company));
        when(technologyRepository.findAllById(invalidIds)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> {
            jobOpeningService.createJobOpening(request);
        });

        verify(technologyRepository).findAllById(invalidIds);
        verify(jobOpeningRepository, never()).save(any(JobOpening.class));
    }

    @Test
    void shouldCreateJobOpeningWithEmptyTechnologiesWhenNull(){
        CreateJobOpeningRequest request = new CreateJobOpeningRequest(
                jobOpening.getTitle(),
                jobOpening.getDescription(),
                jobOpening.getLevel(),
                jobOpening.getWorkMode(),
                jobOpening.getJobUrl(),
                jobOpening.getPostedAt(),
                jobOpening.getCompany().getId(),
                null
        );
        when(companyRepository.findById(jobOpening.getCompany().getId())).thenReturn(Optional.of(company));
        when(jobOpeningRepository.save(any(JobOpening.class))).thenAnswer(
                invocation -> invocation.getArgument(0));

        JobOpeningResponse response = jobOpeningService.createJobOpening(request);

        assertNotNull(response);
        assertTrue(response.technologies().isEmpty());

        verify(companyRepository).findById(jobOpening.getCompany().getId());
        verify(jobOpeningRepository).save(any(JobOpening.class));
        verify(technologyRepository, never()).findAllById(any());
    }

    @Test
    void shouldFindJobOpeningById(){
        Long existingId = 1L;

        when(jobOpeningRepository.findById(existingId)).thenReturn(Optional.of(jobOpening));

        JobOpeningResponse response = jobOpeningService.findJobOpeningById(existingId);

        assertNotNull(response);
        assertEquals(jobOpening.getId(), response.id());
        assertEquals(jobOpening.getTitle(), response.title());
        assertEquals(jobOpening.getCompany().getId(), response.companyId());
        assertEquals(jobOpening.getCompany().getName(), response.companyName());
        assertEquals(jobOpening.getTechnologies().size(), response.technologies().size());

        verify(jobOpeningRepository).findById(existingId);
    }

    @Test
    void shouldThrowResourceNotFoundWhenJobOpeningDoesNotExist(){
        Long nonExistingId = 999L;

        when(jobOpeningRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            jobOpeningService.findJobOpeningById(nonExistingId);
        });

        verify(jobOpeningRepository).findById(nonExistingId);
    }

    @Test
    void shouldUpdateJobOpeningSuccessfully(){
        UpdateJobOpeningRequest request = new UpdateJobOpeningRequest(
                "Update",
                "Updated Desc",
                "Level Update",
                WorkMode.ONSITE,
                "updateURL",
                LocalDate.now().plusDays(3),
                jobOpening.getCompany().getId(),
                jobOpening.getTechnologies().stream().map(Technology::getId).collect(Collectors.toSet())
        );

        when(companyRepository.findById(request.companyId())).thenReturn(Optional.of(company));
        when(technologyRepository.findAllById(request.technologyIds())).thenReturn(new  ArrayList<>(technologies));
        when(jobOpeningRepository.findById(jobOpening.getId())).thenReturn(Optional.of(jobOpening));
        when(jobOpeningRepository.save(any(JobOpening.class))).thenAnswer(i -> i.getArgument(0));

        JobOpeningResponse response = jobOpeningService.updateJobOpening(jobOpening.getId(), request);

        assertNotNull(response);
        assertEquals(request.title(), response.title());
        assertEquals(request.companyId(), response.companyId());
        assertEquals(request.companyId(), response.companyId());
        assertEquals(request.technologyIds().size(), response.technologies().size());
        assertEquals(request.description(), response.description());
        assertEquals(request.level(), response.level());
        assertEquals(request.workMode(), response.workMode());

        verify(jobOpeningRepository).findById(jobOpening.getId());
        verify(jobOpeningRepository).save(any(JobOpening.class));
    }

    @Test
    void shouldDeleteJobOpeningWhenExists(){
        Long existingId = 1L;

        when(jobOpeningRepository.findById(existingId)).thenReturn(Optional.of(jobOpening));

        jobOpeningService.deleteJobOpening(existingId);

        verify(jobOpeningRepository).delete(jobOpening);
    }

    @Test
    void shouldThrowResourceNotFoundWhenDeletingNonExistingJobOpening(){
        Long nonExistingId = 999L;

        when(jobOpeningRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            jobOpeningService.deleteJobOpening(nonExistingId);
        });

        verify(jobOpeningRepository).findById(nonExistingId);
        verify(jobOpeningRepository, never()).delete(jobOpening);
    }
}
