INSERT INTO users (id, name, email, password, role)
VALUES (
           uuid_generate_v4(),
           'Admin',
           'admin@medly.com',
           '$2a$10$T77pfdXhQ5lzAi2k2/RR9O6kx3z19nnoaWMvR2Gab/jYQijioT2ru',
           'ADMIN'
       );
