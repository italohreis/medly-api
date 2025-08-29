CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO users (id, name, email, password, role, active)
VALUES
    ('00000000-0000-0000-0000-000000000001', 'Admin Medly', 'admin@medly.com', '$2y$10$t57CEzaNS1/8lC3qvm94z.PG6lFx/Oi1vSo2oYVk9rn4LR7BXoseS', 'ADMIN', true)
    ON CONFLICT (email) DO UPDATE SET
    password = EXCLUDED.password,
                               active = EXCLUDED.active;

INSERT INTO users (id, name, email, password, role, active)
VALUES
    ('00000000-0000-0000-0000-000000000002', 'Dra. House', 'doctor.house@medly.com', '$2y$10$t57CEzaNS1/8lC3qvm94z.PG6lFx/Oi1vSo2oYVk9rn4LR7BXoseS', 'DOCTOR', true)
    ON CONFLICT (email) DO UPDATE SET
    password = EXCLUDED.password,
                               active = EXCLUDED.active;

INSERT INTO users (id, name, email, password, role, active)
VALUES
    ('00000000-0000-0000-0000-000000000003', 'Paciente Zero', 'patient.zero@medly.com', '$2y$10$t57CEzaNS1/8lC3qvm94z.PG6lFx/Oi1vSo2oYVk9rn4LR7BXoseS', 'PATIENT', true)
    ON CONFLICT (email) DO UPDATE SET
    password = EXCLUDED.password,
                               active = EXCLUDED.active;

INSERT INTO doctor (id, name, crm, specialty, user_id)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'Dra. House', '12345-SP', 'NEUROLOGY', '00000000-0000-0000-0000-000000000002')
    ON CONFLICT (id) DO NOTHING;

INSERT INTO patient (id, name, cpf, birth_date, user_id)
VALUES
    ('22222222-2222-2222-2222-222222222222', 'Paciente Zero', '11122233344', '1985-01-15', '00000000-0000-0000-0000-000000000003')
    ON CONFLICT (id) DO NOTHING;