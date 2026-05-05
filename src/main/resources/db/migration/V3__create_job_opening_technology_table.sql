CREATE TABLE job_opening_technology (
    job_opening_id BIGINT NOT NULL,
    technology_id BIGINT NOT NULL,

    CONSTRAINT pk_job_opening_technology
        PRIMARY KEY (job_opening_id, technology_id),

    CONSTRAINT fk_job_opening_technology_job_opening
        FOREIGN KEY (job_opening_id)
            REFERENCES job_opening(id),

    CONSTRAINT fk_job_opening_technology_technology
        FOREIGN KEY (technology_id)
            REFERENCES technology(id)
);