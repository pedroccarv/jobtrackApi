package com.pedro.jobtrackapi.repository;

import com.pedro.jobtrackapi.model.Technology;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnologyRepository extends JpaRepository<Technology, Long> {
}
