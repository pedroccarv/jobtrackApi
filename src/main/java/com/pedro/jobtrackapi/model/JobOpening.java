package com.pedro.jobtrackapi.model;

import com.pedro.jobtrackapi.enums.WorkMode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(name = "job_opening_technology",
            joinColumns = @JoinColumn(name = "job_opening_id"),
            inverseJoinColumns = @JoinColumn(name = "technology_id"))
    private Set<Technology> technologies = new HashSet<>();

}
