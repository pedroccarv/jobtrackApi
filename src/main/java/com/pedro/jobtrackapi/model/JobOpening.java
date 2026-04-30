package com.pedro.jobtrackapi.model;

import com.pedro.jobtrackapi.enums.WorkMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class JobOpening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String level;

    @Enumerated(EnumType.STRING)
    private WorkMode workMode;

    private String jobUrl;

    private LocalDate postedAt;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

}
