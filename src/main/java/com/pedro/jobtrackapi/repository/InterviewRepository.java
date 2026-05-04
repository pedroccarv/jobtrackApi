package com.pedro.jobtrackapi.repository;

import com.pedro.jobtrackapi.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
}
