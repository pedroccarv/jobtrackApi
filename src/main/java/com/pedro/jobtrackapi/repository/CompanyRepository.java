package com.pedro.jobtrackapi.repository;

import com.pedro.jobtrackapi.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
