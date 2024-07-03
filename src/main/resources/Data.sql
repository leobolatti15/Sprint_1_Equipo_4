-- Insertando el primer hotel
INSERT INTO hotels (hotel_code, name, destination, room_type, price_per_night, date_from, date_to, reserved)
VALUES ('CH-0002', 'Cataratas Hotel', 'Puerto Iguazu', 'Double', 6300, '2025-02-10', '2025-03-20', 0);

-- Insertando el segundo hotel
INSERT INTO hotels (hotel_code, name, destination, room_type, price_per_night, date_from, date_to, reserved)
VALUES ('CH-0003', 'Cataratas Hotel 2', 'Puerto Iguazu', 'Triple', 8200, '2025-02-10', '2025-03-23', 0);

-- Insertando el tercer hotel
INSERT INTO hotels (hotel_code, name, destination, room_type, price_per_night, date_from, date_to, reserved)
VALUES ('HB-0001', 'Hotel Bristol', 'Buenos Aires', 'Single', 5435, '2025-02-10', '2025-03-19', 1);

-- Insertando el cuarto hotel
INSERT INTO hotels (hotel_code, name, destination, room_type, price_per_night, date_from, date_to, reserved)
VALUES ('BH-0002', 'Hotel Bristol 2', 'Buenos Aires', 'Double', 7200, '2025-02-12', '2025-04-17', 0);

-- Insertando el primer vuelo
INSERT INTO flights (flight_number, origin, destination, seat_type, price_per_person, date_from, date_to)
VALUES ('BAPI-1235', 'Buenos Aires', 'Puerto Iguazu', 'Economy', 6500, '2025-02-10', '2025-02-15');

-- Insertando el segundo vuelo
INSERT INTO flights (flight_number, origin, destination, seat_type, price_per_person, date_from, date_to)
VALUES ('PIBA-1420', 'Puerto Iguazu', 'Bogota', 'Business', 43200, '2025-02-10', '2025-02-20');

-- Insertando el tercer vuelo
INSERT INTO flights (flight_number, origin, destination, seat_type, price_per_person, date_from, date_to)
VALUES ('PIBA-1420', 'Puerto Iguazu', 'Bogota', 'Economy', 25735, '2025-02-10', '2025-02-21');

-- Insertando el cuarto vuelo
INSERT INTO flights (flight_number, origin, destination, seat_type, price_per_person, date_from, date_to)
VALUES ('BATU-5536', 'Buenos Aires', 'Tucuman', 'Economy', 7320, '2025-02-10', '2025-02-17');