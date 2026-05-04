package com.pedro.jobtrackapi.repository;

import com.pedro.jobtrackapi.model.JobApplication;
import com.pedro.jobtrackapi.model.JobOpening;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    boolean existsByJobOpening(JobOpening jobOpening);
}
