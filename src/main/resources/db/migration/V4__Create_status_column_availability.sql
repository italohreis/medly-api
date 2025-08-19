ALTER TABLE availability ADD COLUMN status VARCHAR(255);

UPDATE availability SET status = 'BOOKED' WHERE is_available = false;
UPDATE availability SET status = 'AVAILABLE' WHERE is_available = true;

ALTER TABLE availability DROP COLUMN is_available;

