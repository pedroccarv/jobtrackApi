package com.pedro.jobtrackapi.model;

import com.pedro.jobtrackapi.enums.InterviewType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_application_id")
    private JobApplication jobApplication;

    private LocalDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    private InterviewType type;

    private String interviewer;

    private String notes;
}
