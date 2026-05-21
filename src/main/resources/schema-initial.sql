CREATE DATABASE IF NOT EXISTS medical_records
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE medical_records;

## Create initial tables ##

CREATE TABLE IF NOT EXISTS doctors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    egn VARCHAR(10) UNIQUE NOT NULL ,
    name VARCHAR(255) NOT NULL ,
    can_be_general_practitioner TINYINT(1) NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS doctor_specialties (
    doctor_id BIGINT NOT NULL,
    specialty VARCHAR(255) NOT NULL,
    CONSTRAINT fk_ds_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

CREATE TABLE IF NOT EXISTS patients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    egn VARCHAR(10) UNIQUE NOT NULL ,
    name VARCHAR(255) NOT NULL,
    general_practitioner_id BIGINT,
    has_health_insurance TINYINT(1) NOT NULL DEFAULT 0,
    CONSTRAINT fk_patient_gp FOREIGN KEY (general_practitioner_id) REFERENCES doctors(id)
);

CREATE TABLE IF NOT EXISTS diagnoses(
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    name VARCHAR(50) NOT NULL UNIQUE ,
    description TEXT
);

CREATE TABLE IF NOT EXISTS checkups(
    id  BIGINT AUTO_INCREMENT PRIMARY KEY ,
    patient_id BIGINT NOT NULL ,
    doctor_id BIGINT NOT NULL ,
    date    DATE NOT NULL ,
    treatment TEXT,
    price DECIMAL(10,2) NOT NULL ,
    paid_by_patient TINYINT(1) NOT NULL DEFAULT 0,
    CONSTRAINT fk_checkup_patient FOREIGN KEY (patient_id) REFERENCES patients(id),
    CONSTRAINT fk_checkup_doctor  FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

CREATE TABLE IF NOT EXISTS checkup_diagnoses (
    checkup_id BIGINT NOT NULL,
    diagnosis_id BIGINT NOT NULL,
    CONSTRAINT fk_cd_checkup    FOREIGN KEY (checkup_id)   REFERENCES checkups(id),
    CONSTRAINT fk_cd_diagnosis  FOREIGN KEY (diagnosis_id) REFERENCES diagnoses(id)
);

CREATE TABLE IF NOT EXISTS sick_leaves(
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    patient_id BIGINT NOT NULL ,
    doctor_id BIGINT NOT NULL ,
    start_date DATE NOT NULL ,
    duration_days INT NOT NULL ,
    CONSTRAINT fk_sick_patient FOREIGN KEY (patient_id) REFERENCES patients(id),
    CONSTRAINT fk_sick_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

CREATE TABLE IF NOT EXISTS app_users(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL ,
    password VARCHAR(200) NOT NULL ,
    role VARCHAR(20) NOT NULL ,
    patient_id BIGINT UNIQUE ,
    doctor_id BIGINT UNIQUE ,
    CONSTRAINT fk_app_patient FOREIGN KEY (patient_id) REFERENCES patients(id),
    CONSTRAINT fk_app_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);


## initial data

-- Doctors:
INSERT INTO doctors (id, egn, name, can_be_general_practitioner) VALUES
(1, '7001015678', 'Ivan Petrov',       1),
(2, '6504123456', 'Maria Kostadinova', 0),
(3, '7203044532', 'Georgi Stoyanov',   1),
(4, '6809271234', 'Elena Nikolova',    0),
(5, '7505097890', 'Petar Dimitrov',    0);

-- Doctor specialties (each doctor now has multiple):
INSERT INTO doctor_specialties (doctor_id, specialty) VALUES
(1, 'FAMILY_MEDICINE'),
(1, 'INTERNAL_MEDICINE'),
(2, 'CARDIOLOGY'),
(2, 'INTERNAL_MEDICINE'),
(3, 'PEDIATRICS'),
(3, 'FAMILY_MEDICINE'),
(4, 'INTERNAL_MEDICINE'),
(4, 'CARDIOLOGY'),
(5, 'NEUROLOGY'),
(5, 'PSYCHIATRY');


-- Patients:
-- has_health_insurance: 1 = insured (insurer pays), 0 = not insured (patient pays)
INSERT INTO patients (id, egn, name, general_practitioner_id, has_health_insurance) VALUES
(1, '9001011234', 'Stefan Angelov',    1, 1),
(2, '8507232345', 'Katerina Ivanova',  1, 0),
(3, '9203154567', 'Dimitar Todorov',   3, 1),
(4, '7810285678', 'Yana Georgieva',    3, 0),
(5, '8812196789', 'Nikolai Petkov',    1, 1),
(6, '9506307890', 'Monika Stancheva',  3, 1),
(7, '7204178901', 'Boris Iliev',       1, 0),
(8, '8903249012', 'Svetla Metodieva',  3, 0);

-- Diagnoses:
INSERT INTO diagnoses (id, name, description) VALUES
(1,  'Arterial Hypertension',           'Persistently elevated blood pressure in the arteries'),
(2,  'Type 2 Diabetes Mellitus',        'Chronic condition affecting blood sugar regulation'),
(3,  'Upper Respiratory Tract Infection', 'Infection of the nose, throat, and airways'),
(4,  'Acute Bronchitis',                'Inflammation of the bronchial tubes'),
(5,  'Lumbar Disc Herniation',          'Rupture or displacement of a lumbar intervertebral disc'),
(6,  'Migraine',                        'Recurrent headaches of moderate to severe intensity'),
(7,  'Generalized Anxiety Disorder',    'Excessive, uncontrollable worry about everyday events'),
(8,  'Coronary Artery Disease',         'Narrowing of coronary arteries reducing blood flow to the heart'),
(9,  'Acute Gastritis',                 'Inflammation of the stomach lining'),
(10, 'Seasonal Influenza',              'Viral respiratory illness caused by influenza viruses');

-- Check-ups (diagnosis_id column removed — diagnoses linked via checkup_diagnoses below)
-- paid_by_patient: 0 = insurance covered, 1 = patient pays
INSERT INTO checkups (id, patient_id, doctor_id, date, treatment, price, paid_by_patient) VALUES
-- Dr. Petrov (family medicine + internal medicine, GP)
( 1, 1, 1, '2025-01-10', 'Amlodipine 5mg once daily, low-salt diet',           25.00, 0),
( 2, 2, 1, '2025-01-14', 'Paracetamol 500mg, bed rest, fluids',                 25.00, 1),
( 3, 5, 1, '2025-01-22', 'Metformin 500mg twice daily, dietary adjustments',    25.00, 0),
( 4, 7, 1, '2025-02-05', 'Ibuprofen 400mg, rest, increased fluid intake',       25.00, 1),
( 5, 1, 1, '2025-03-01', 'Increase Amlodipine to 10mg, follow-up in 1 month',  25.00, 0),

-- Dr. Kostadinova (cardiology + internal medicine)
( 6, 1, 2, '2025-01-18', 'Aspirin 100mg, Atorvastatin 20mg, cardiac monitoring', 80.00, 0),
( 7, 5, 2, '2025-02-12', 'Bisoprolol 5mg, ECG follow-up in 3 months',           80.00, 0),
( 8, 4, 2, '2025-03-08', 'Enalapril 10mg once daily',                            80.00, 1),

-- Dr. Stoyanov (pediatrics + family medicine, GP)
( 9, 3, 3, '2025-01-07', 'Amoxicillin 250mg three times daily for 7 days',      30.00, 0),
(10, 4, 3, '2025-01-25', 'Rest, fluids, Vitamin C supplementation',              30.00, 1),
(11, 6, 3, '2025-02-18', 'Azithromycin 250mg for 5 days, inhaler if needed',    30.00, 0),
(12, 8, 3, '2025-03-03', 'Paracetamol syrup, nasal drops, bed rest',            30.00, 1),

-- Dr. Nikolova (internal medicine + cardiology)
(13, 2, 4, '2025-02-20', 'Omeprazole 20mg before meals, bland diet for 2 weeks', 60.00, 1),
(14, 6, 4, '2025-03-12', 'Insulin therapy initiation, nutritionist referral',    60.00, 0),

-- Dr. Dimitrov (neurology + psychiatry)
(15, 3, 5, '2025-02-27', 'Sumatriptan 50mg at onset, avoid triggers',            70.00, 0);

-- Check-up diagnoses (multiple diagnoses per check-up where clinically realistic):
INSERT INTO checkup_diagnoses (checkup_id, diagnosis_id) VALUES
( 1, 1),        -- Hypertension
( 2, 3),        -- Upper respiratory infection
( 3, 2),        -- Diabetes
( 3, 1),        -- + Hypertension (common comorbidity)
( 4, 10),       -- Influenza
( 5, 1),        -- Hypertension follow-up
( 6, 8),        -- Coronary artery disease
( 6, 1),        -- + Hypertension (common comorbidity)
( 7, 8),        -- Coronary artery disease
( 8, 1),        -- Hypertension
( 9, 3),        -- Upper respiratory infection
(10, 10),       -- Influenza
(11, 4),        -- Acute bronchitis
(11, 3),        -- + Upper respiratory infection
(12, 3),        -- Upper respiratory infection
(13, 9),        -- Acute gastritis
(14, 2),        -- Diabetes
(15, 6);        -- Migraine


-- Sick leaves
INSERT INTO sick_leaves (id, patient_id, doctor_id, start_date, duration_days) VALUES
-- January 2025 (most sick leaves → wins the statistic)
(1, 1, 1, '2025-01-11',  7),   -- Stefan, Dr. Petrov
(2, 3, 3, '2025-01-20', 10),   -- Dimitar, Dr. Stoyanov
(3, 7, 1, '2025-01-27',  5),   -- Boris, Dr. Petrov
(4, 4, 3, '2025-01-09',  3),   -- Yana, Dr. Stoyanov

-- February 2025
(5, 2, 1, '2025-02-04',  5),   -- Katerina, Dr. Petrov
(6, 5, 1, '2025-02-28',  7),   -- Nikolai, Dr. Petrov
(7, 8, 3, '2025-02-14', 14),   -- Svetla, Dr. Stoyanov

-- March 2025
(8, 6, 3, '2025-03-15',  3);   -- Monika, Dr. Stoyanov

-- Users
-- Passwords are BCrypt(cost=10). Plaintext → "admin" for admin, "password" for everyone else.
INSERT INTO app_users (id, username, password, role, doctor_id, patient_id) VALUES
(2, 'dr.petrov',   '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'DOCTOR',  1,    NULL),
(3, 'dr.kost',     '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'DOCTOR',  2,    NULL),
(4, 'dr.stoyanov', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'DOCTOR',  3,    NULL),
(5, 'dr.nikolova', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'DOCTOR',  4,    NULL),
(6, 'dr.dimitrov', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'DOCTOR',  5,    NULL),
(7, 'stefan',      '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'PATIENT', NULL, 1),
(8, 'katerina',    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'PATIENT', NULL, 2);
