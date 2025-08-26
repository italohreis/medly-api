-- Limpa os agendamentos antigos que são incompatíveis com a nova estrutura
DELETE FROM appointment;

-- Remove a antiga coluna e constraint de chave estrangeira da tabela 'appointment'
ALTER TABLE appointment DROP CONSTRAINT IF EXISTS uk_appointment_availability_id;
ALTER TABLE appointment DROP COLUMN IF EXISTS availability_id;

-- Renomeia a tabela 'availability' para 'availability_windows', se ela existir
ALTER TABLE IF EXISTS availability RENAME TO availability_windows;

-- Remove a coluna 'status' da nova tabela 'availability_windows'
ALTER TABLE IF EXISTS availability_windows DROP COLUMN IF EXISTS status;

-- Cria a nova tabela 'time_slots', se ela não existir
CREATE TABLE IF NOT EXISTS time_slots (
                                          id UUID PRIMARY KEY,
                                          start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                          end_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                          status VARCHAR(255) NOT NULL,
    availability_window_id UUID NOT NULL,
    CONSTRAINT fk_timeslot_to_availability_window FOREIGN KEY (availability_window_id) REFERENCES availability_windows(id)
    );

-- Adiciona a nova coluna 'time_slot_id' na tabela 'appointment', se não existir
ALTER TABLE appointment ADD COLUMN IF NOT EXISTS time_slot_id UUID;

-- Adiciona a constraint de chave estrangeira, se não existir (o nome da constraint deve ser único)
ALTER TABLE appointment DROP CONSTRAINT IF EXISTS fk_appointment_to_time_slot;
ALTER TABLE appointment ADD CONSTRAINT fk_appointment_to_time_slot FOREIGN KEY (time_slot_id) REFERENCES time_slots(id);

-- Adiciona a constraint UNIQUE, se não existir
ALTER TABLE appointment DROP CONSTRAINT IF EXISTS uk_appointment_time_slot_id;
ALTER TABLE appointment ADD CONSTRAINT uk_appointment_time_slot_id UNIQUE (time_slot_id);