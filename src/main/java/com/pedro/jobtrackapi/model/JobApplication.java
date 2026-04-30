package com.pedro.jobtrackapi.model;

import com.pedro.jobtrackapi.enums.ApplicationStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity

public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_opening_id")
    private JobOpening jobOpening;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private String notes;

    private LocalDate appliedAt;


}
