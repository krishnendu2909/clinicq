drop database if exists hospital_db;

CREATE DATABASE hospital_db;

USE hospital_db;

CREATE TABLE users
(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	email VARCHAR(100) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL,
	role ENUM('ADMIN','DOCTOR','RECEPTIONIST','PATIENT') NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	
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
	status ENUM('BOOKED','CHECKED_IN','COMPLETED','CANCELLED','NO_SHOW'),
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
	patient_id BIGINT,
	doctor_id BIGINT,
	appointment_id BIGINT,
	date DATE,
	source ENUM('APPOINTMENT','WALK_IN'),	
	status ENUM('WAITING','IN_CONSULTATION','COMPLETED','NO_SHOW'),
	check_in_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

	FOREIGN KEY (patient_id) REFERENCES patients(id),
	FOREIGN KEY (doctor_id) REFERENCES doctors(id),
	FOREIGN KEY (appointment_id) REFERENCES appointments(id)
);
CREATE TABLE admin
(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	user_id BIGINT,
	
	max_patients_per_slot INT DEFAULT 1,
	max_days_in_advance INT DEFAULT 7,
	
	cancellation_cutoff_hours INT DEFAULT 2,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	
	FOREIGN KEY (user_id) REFERENCES users(id)
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

-- Insert sample data 
INSERT INTO users (email, password, role, created_at)
VALUES
('admin@example.com', 'securepassword1', 'ADMIN', CURRENT_TIMESTAMP),
('doctor1@example.com', 'securepassword2', 'DOCTOR', CURRENT_TIMESTAMP),
('doctor2@example.com', 'securepassword3', 'DOCTOR', CURRENT_TIMESTAMP),
('receptionist@example.com', 'securepassword4', 'RECEPTIONIST', CURRENT_TIMESTAMP),
('patient1@example.com', 'securepassword5', 'PATIENT', CURRENT_TIMESTAMP),
('patient2@example.com', 'securepassword6', 'PATIENT', CURRENT_TIMESTAMP),
('patient3@example.com', 'securepassword7', 'PATIENT', CURRENT_TIMESTAMP),
('doctor3@example.com', 'securepassword8', 'DOCTOR', CURRENT_TIMESTAMP),
('doctor4@example.com', 'securepassword9', 'DOCTOR', CURRENT_TIMESTAMP),
('patient4@example.com', 'securepassword10', 'PATIENT', CURRENT_TIMESTAMP);

INSERT INTO doctors (user_id, name, department, gender, phone, location, description)
VALUES
(2, 'Dr. John Smith', 'CARDIOLOGY', 'MALE', '1234567890', 'R-101', 'Experienced cardiologist'),
(3, 'Dr. Emily Johnson', 'ORTHOPEDICS', 'FEMALE', '0987654321', 'R-102', 'Expert in orthopedic surgeries'),
(8, 'Dr. Michael Brown', 'PEDIATRICS', 'MALE', '1122334455', 'R-103', 'Specializes in child healthcare'),
(9, 'Dr. Sarah Davis', 'GENERAL', 'FEMALE', '2233445566', 'R-104', 'Provides general medical care'),
(2, 'Dr. William Wilson', 'CARDIOLOGY', 'MALE', '3344556677', 'R-105', 'Expert in heart surgeries'),
(3, 'Dr. Olivia Martinez', 'ORTHOPEDICS','FEMALE', '4455667788', 'R-106', 'Specializes in joint replacement'),
(8, 'Dr. James Garcia', 'PEDIATRICS', 'MALE','5566778899', 'R-107', 'Focuses on newborn care'),
(9, 'Dr. Sophia Lee', 'GENERAL', 'FEMALE', '6677889900', '108', 'Provides family healthcare'),
(2, 'Dr. Benjamin Taylor', 'CARDIOLOGY',  'MALE', '7788990011', 'R-109', 'Specializes in heart rhythm disorders'),
(3, 'Dr. Isabella Moore', 'ORTHOPEDICS','FEMALE', '8899001122', 'R-110', 'Expert in spinal surgeries');

INSERT INTO patients (user_id, name, date_of_birth, gender, phone)
VALUES
(5, 'Alice Johnson', '1990-05-15', 'FEMALE', '1234567890'),
(6, 'Bob Smith', '1985-08-22', 'MALE', '9876543210'),
(7, 'Charlie Brown', '2000-01-10', 'MALE', '1122334455'),
(10, 'Daisy Miller', '1995-03-25', 'FEMALE', '2233445566');

INSERT INTO time_slots (doctor_id, slot_date, start_time, end_time, booked, created_at)
VALUES
(1, '2026-04-23', '09:00:00', '09:30:00', FALSE, CURRENT_TIMESTAMP),
(1, '2026-04-23', '09:30:00', '10:00:00', TRUE, CURRENT_TIMESTAMP),
(2, '2026-04-23', '10:00:00', '10:30:00', FALSE, CURRENT_TIMESTAMP),
(2, '2026-04-23', '10:30:00', '11:00:00', TRUE, CURRENT_TIMESTAMP),
(3, '2026-04-23', '11:00:00', '11:30:00', FALSE, CURRENT_TIMESTAMP),
(3, '2026-04-23', '11:30:00', '12:00:00', TRUE, CURRENT_TIMESTAMP),
(4, '2026-04-23', '14:00:00', '14:30:00', FALSE, CURRENT_TIMESTAMP),
(4, '2026-04-23', '14:30:00', '15:00:00', TRUE, CURRENT_TIMESTAMP),
(5, '2026-04-23', '15:00:00', '15:30:00', FALSE, CURRENT_TIMESTAMP),
(5, '2026-04-23', '15:30:00', '16:00:00', TRUE, CURRENT_TIMESTAMP);

INSERT INTO time_slots(doctor_id, slot_date, start_time, end_time, booked) 
VALUES
(1,'2026-04-22','09:00:00','09:30:00',0),
(1,'2026-04-22','10:00:00','10:30:00',0);
select * from users;
select * from patients;
select * from doctors;
select * from time_slots;
select * from appointments;
select * from tokens;
select * from admin;
select * from doctor_schedule;