CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE doctor (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    crm VARCHAR(255) UNIQUE NOT NULL,
    specialty VARCHAR(255),
    user_id UUID UNIQUE NOT NULL,
    CONSTRAINT fk_doctor_to_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE patient (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cpf VARCHAR(20) UNIQUE,
    birth_date DATE,
    user_id UUID UNIQUE NOT NULL,
    CONSTRAINT fk_patient_to_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE availability_windows (
    id UUID PRIMARY KEY,
    start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    doctor_id UUID NOT NULL,
    CONSTRAINT fk_window_to_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id)
);

CREATE TABLE time_slots (
    id UUID PRIMARY KEY,
    start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status VARCHAR(255) NOT NULL,
    availability_window_id UUID NOT NULL,
    CONSTRAINT fk_timeslot_to_availability_window FOREIGN KEY (availability_window_id) REFERENCES availability_windows(id)
);

CREATE TABLE appointment (
    id UUID PRIMARY KEY,
    status VARCHAR(20) NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT false,
    doctor_id UUID NOT NULL,
    patient_id UUID NOT NULL,
    time_slot_id UUID UNIQUE,
    CONSTRAINT fk_appointment_to_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id),
    CONSTRAINT fk_appointment_to_patient FOREIGN KEY (patient_id) REFERENCES patient(id),
    CONSTRAINT fk_appointment_to_time_slot FOREIGN KEY (time_slot_id) REFERENCES time_slots(id)
);