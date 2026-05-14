drop database if exists hospital_db;



CREATE DATABASE hospital_db;



USE hospital_db;



CREATE TABLE users

(

id BIGINT PRIMARY KEY AUTO_INCREMENT,

email VARCHAR(100) UNIQUE NOT NULL,

password VARCHAR(255) NOT NULL,

role ENUM('ADMIN','DOCTOR','RECEPTIONIST','PATIENT') NOT NULL,

created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

reset_token VARCHAR(255),

token_expiry DATETIME,

failed_attempts INT DEFAULT 0,

account_locked BOOLEAN DEFAULT FALSE,

lock_time DATETIME





);



CREATE TABLE patients

(

id BIGINT PRIMARY KEY AUTO_INCREMENT,

user_id BIGINT,

name VARCHAR(100) NOT NULL,

date_of_birth DATE,

gender ENUM('MALE','FEMALE','OTHER'),

phone VARCHAR(20),

FOREIGN KEY (user_id) REFERENCES users(id)

);



CREATE TABLE doctors

(

id BIGINT PRIMARY KEY AUTO_INCREMENT,

user_id BIGINT,

name VARCHAR(100) NOT NULL,

department ENUM('CARDIOLOGY','ORTHOPEDICS','PEDIATRICS','GENERAL') ,

gender ENUM('MALE','FEMALE','OTHER'),

phone VARCHAR(20),

location VARCHAR(100),

description VARCHAR(250),

FOREIGN KEY (user_id) REFERENCES users(id)

);



CREATE TABLE time_slots

(

id BIGINT PRIMARY KEY AUTO_INCREMENT,

doctor_id BIGINT,

slot_date DATE,

start_time TIME,

end_time TIME,

booked BOOLEAN DEFAULT FALSE,

created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,



FOREIGN KEY (doctor_id) REFERENCES doctors(id)



);



CREATE TABLE appointments

(

id BIGINT PRIMARY KEY AUTO_INCREMENT,

patient_id BIGINT,

doctor_id BIGINT,

time_slot_id BIGINT,

reason VARCHAR(300),

status ENUM('BOOKED','CHECKED_IN','COMPLETED','CANCELLED','NO_SHOW','IN_CONSULTATION'),

type ENUM('WALK_IN','PRE_BOOKED'),

created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,



FOREIGN KEY (patient_id) REFERENCES patients(id),

FOREIGN KEY (doctor_id) REFERENCES doctors(id),

FOREIGN KEY (time_slot_id) REFERENCES time_slots(id)

);



CREATE TABLE tokens

(

id BIGINT PRIMARY KEY AUTO_INCREMENT,

token_number INT,

token_display VARCHAR(20),

patient_id BIGINT,

doctor_id BIGINT,

appointment_id BIGINT,

date DATE,

status ENUM('WAITING','IN_CONSULTATION','COMPLETED','NO_SHOW'),

check_in_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

position INT,

FOREIGN KEY (patient_id) REFERENCES patients(id),

FOREIGN KEY (doctor_id) REFERENCES doctors(id),

FOREIGN KEY (appointment_id) REFERENCES appointments(id)

);

CREATE TABLE admin

(

id BIGINT PRIMARY KEY AUTO_INCREMENT,

max_patients_per_slot INT DEFAULT 1,

max_days_in_advance INT DEFAULT 7,

cancellation_cutoff_hours INT DEFAULT 2,

created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP



);



CREATE TABLE doctor_schedule

(

id BIGINT PRIMARY KEY AUTO_INCREMENT,

doctor_id BIGINT,



day_of_week ENUM('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY'),

start_time TIME,

end_time TIME,

slot_duration INT,



created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

FOREIGN KEY (doctor_id) REFERENCES doctors(id)

);



CREATE TABLE prescription

(

id BIGINT PRIMARY KEY AUTO_INCREMENT,

appointment_id BIGINT,

diagnosis TEXT,

notes TEXT,

status ENUM('DRAFT','FINAL'),

created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

FOREIGN KEY (appointment_id) REFERENCES appointments(id)

);



CREATE TABLE prescription_item

(

id BIGINT PRIMARY KEY AUTO_INCREMENT,

prescription_id BIGINT,

medicine_name VARCHAR(100),

dosage VARCHAR(50),

frequency VARCHAR(50),

duration VARCHAR(50),



FOREIGN KEY (prescription_id) REFERENCES prescription(id)

);







select * from users;

select * from patients;

select * from doctors;

select * from time_slots;

select * from appointments;

select * from tokens;

select * from admin;

select * from doctor_schedule;