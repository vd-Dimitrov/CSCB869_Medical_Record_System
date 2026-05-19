# Medical Records System

This is a web-based medical record management system. It is built in Java 17 using Spring Boot 3, Thymeleaf ofr the frontend and REST API.
## Technologies used:

- Language: Java 17
- Framework: Spring Boot 3
- Security: Spring Security 6
- Persistence: Spring Data JPA, Hibernate, MySQL
- Validation: Jakarta Bean Validation
- Frontend: Thymeleaf
- Frontend template: Bootswatch Flatly
- Extras: Lombok (in place of boilerplate code)

## Prerequisites

- Java 17+
- MySQL 8+ running on `localhost:3306`
- Gradle (wrapper included)

## Setup

**1. Configure the database connection**

Head to `src/main/resources/application.properties`, adjust `username` and `password` for your database settings.

```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

The database `medical_records` is created automatically on the first boot (`createDatabaseIfNotExist=true`).
Schema is managed by Hibernate (`ddl-auto=update`).

**2. Build and run**

```bash
./gradlew bootRun        # Linux / macOS
gradlew.bat bootRun      # Windows
tasks/build/build        # IntelliJ interface
```

The application is accessed at `http://localhost:8080` by default.

**3. Seed example data**

After the first boot has created the tables, run the SQL file `src/main/resources/schema-seed.sql` against the `medical_records` database. This populates 5 doctors, 8 patients, 10 diagnoses, 15 check-ups, 8 sick leaves, and 8 user accounts, enough to showcase the program.

## Default Credentials

The usernames can be found in the `app_users` table and all users are initialized with default password: `password`

The `admin` account is created automatically by `DataInitializer` on first boot if it does not already exist.

## Domain Model

```
Person (abstract) - EGN, Name
├── Doctor  — specialty, canBeGeneralPractitioner
└── Patient — generalPractitioner (→ Doctor), hasHealthInsurance

Diagnosis   — name (unique), description

CheckUp     — patient, doctor, diagnosis, date, treatment, price, paidByPatient
                -  paidByPatient is auto-set at creation: true if patient is uninsured

SickLeave   — patient, doctor, startDate, durationDays

AppUser     — username, password (BCrypt), role (ADMIN/DOCTOR/PATIENT)
                -  optional one-to-one link to Doctor or Patient
```

## Access Control

- Patient: Can view only their own check-ups and sick leaves
- Doctor: can view all data, can edit only check-ups and sick leaves they are accountable for
- Admin: full access, plus user management
-
Ownership is enforced at three layers: the Thymeleaf template (GUI), the controller (direct URL access), and the service (POST requests).

## MVC Routes

- `/doctors` - ADMIN, DOCTOR
- `/doctors/new`, `/doctors/*/edit`, `/doctors/*/delete` - ADMIN
- `/patients` - ADMIN, DOCTOR
- `/patients/new`, `/patients/*/edit`, `/patients/*/delete` - ADMIN
- `/checkups` - All authenticated
- `/checkups/new`, `/checkups/*/edit` - ADMIN, DOCTOR (own only for DOCTOR)
- `/checkups/*/delete` - ADMIN
- `/sickleaves` - All authenticated
- `/sickleaves/new`, `/sickleaves/*/edit` - ADMIN, DOCTOR (own only for DOCTOR)
- `/sickleaves/*/delete` - ADMIN
- `/diagnoses` - All authenticated
- `/diagnoses/new`, `/diagnoses/*/edit`, `/diagnoses/*/delete` - ADMIN
- `/statistics` - ADMIN, DOCTOR
- `/admin` - ADMIN

## REST API

Base path: `/api/...`  
Authentication: HTTP Basic

| Endpoint          | GET                    | POST          | PUT           | DELETE |
|-------------------|------------------------|---------------|---------------|--------|
| `/api/doctors`    | Any                    | ADMIN         | ADMIN         | ADMIN  |
| `/api/patients`   | All (PATIENT only own) | ADMIN         | ADMIN         | ADMIN  |
| `/api/diagnoses`  | Any                    | ADMIN         | ADMIN         | ADMIN  |
| `/api/checkups`   | All (PATIENT only own) | ADMIN, DOCTOR | ADMIN, DOCTOR | ADMIN  |
| `/api/sickleaves` | All (PATIENT only own) | ADMIN, DOCTOR | ADMIN, DOCTOR | ADMIN  |


## Statistics (`/statistics`)

The statistics cover the following parameters:
- Which are the most common diagnosis
- What is the total amount paid by patients
- Display how much each doctor has been patient-paid
- Visit count for each doctor
- Patient count per general practitioner
- Which month has the most sick leaves issued
- Doctor(s) who issued the most sick leaves
- Patient visit history (filterable by patient)
- Display patients by diagnosis
- Display patients by General Practitioner
- Check-ups by doctor and date range

## Project Structure

```
src/main/java/.../
├── config/          SecurityConfig, DataInitializer
├── data/
│   ├── dto/         Form-binding DTOs (Bean Validation annotations)
│   ├── entity/      JPA entities (Doctor, Patient, Diagnosis, CheckUp, SickLeave, AppUser)
│   ├── enums/       UserRole, MedicalSpecialty
│   └── repository/  Spring Data JPA repositories + custom JPQL queries
├── exception/       ResourceNotFoundException, AccessDeniedException, GlobalExceptionHandler, RestExceptionHandler
├── security/        SecurityUser (UserDetails impl), UserDetailsServiceImpl
├── service/         Service interfaces + implementations
└── web/
    ├── controller/  Thymeleaf MVC controllers
    └── rest/        REST controllers

src/main/resources/
├── static/
│   ├── css/         bootstrap.css (Bootswatch Flatly, trimmed), app.css (custom styles)
│   └── js/          app.js (client-side table sorting)
└── templates/
    ├── fragments/   layout.html (head/scripts), navbar.html
    ├── doctors/     list, form, detail
    ├── patients/    list, form, detail
    ├── checkups/    list, form, detail
    ├── sickleaves/  list, form
    ├── diagnoses/   list, form
    ├── statistics/  index
    ├── admin/       list, form
    ├── login.html
    ├── index.html
    └── error.html
```