package com.pedro.jobtrackapi.repository;

import com.pedro.jobtrackapi.model.Company;
import com.pedro.jobtrackapi.model.JobOpening;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JobOpeningRepository extends JpaRepository<JobOpening, Long> {
    public Company findCompanyById(Long id);
}
