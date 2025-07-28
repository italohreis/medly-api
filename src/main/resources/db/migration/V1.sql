CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE "users" (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE doctor (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255),
    user_id UUID UNIQUE REFERENCES "users"(id)
);

CREATE TABLE patient (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255),
    cpf VARCHAR(20),
    birth_date DATE,
    user_id UUID UNIQUE REFERENCES "users"(id),
    doctor_id UUID UNIQUE REFERENCES doctor(id)
);

CREATE TABLE appointment (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    date TIMESTAMP NOT NULL,
    doctor_id UUID REFERENCES doctor(id),
    patient_id UUID REFERENCES patient(id),
    status VARCHAR(20)
);