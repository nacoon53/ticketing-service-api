insert into user(user_id, deposit) values('test', 10000);

INSERT INTO concert (concert_id, show_date, concert_name, host)
VALUES (NULL, '2025-01-16 19:30:00', 'CNBLUE CONCERT', 'CNBLUE');


INSERT INTO concert_seat (price, seat_number, concert_id, hold_expired_at, seat_id, seat_status)
VALUES
    (10000.00, 1, 1, '2025-01-16 20:00:00', NULL, 'AVAILABLE'),
    (10000.00, 2, 1, '2025-01-16 20:00:00', NULL, 'AVAILABLE'),
    (10000.00, 3, 1, '2025-01-16 20:00:00', NULL, 'AVAILABLE'),
    (10000.00, 4, 1, '2025-01-16 20:00:00', NULL, 'AVAILABLE'),
    (10000.00, 5, 1, '2025-01-16 20:00:00', NULL, 'AVAILABLE'),
    (10000.00, 6, 1, '2025-01-16 20:00:00', NULL, 'AVAILABLE'),
    (10000.00, 7, 1, '2025-01-16 20:00:00', NULL, 'AVAILABLE'),
    (10000.00, 8, 1, '2025-01-16 20:00:00', NULL, 'AVAILABLE'),
    (10000.00, 9, 1, '2025-01-16 20:00:00', NULL, 'AVAILABLE'),
    (10000.00, 10, 1, '2025-01-16 20:00:00', NULL, 'AVAILABLE');
