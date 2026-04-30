# Business Rules

## Company

- A company must have a name.
- A company may have a LinkedIn URL.
- A company may have a location.
- A company may have notes.

## JobOpening

- A job opening must be linked to a company.
- A job opening must have a title.
- A job opening may have technologies.
- A job opening can exist without a job application.

## JobApplication

- A job application must be linked to a job opening.
- A new job application starts with status APPLIED.
- A job opening should not have duplicate applications.
- The application date is required.

## Interview

- An interview must be linked to a job application.
- An interview must have a scheduled date and time.
- An interview cannot be scheduled in the past.
- When an interview is created, the job application status changes to INTERVIEW.