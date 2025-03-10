INSERT INTO users (id, email, name, password, phone, role, version, actual)
VALUES
    ('43ef2468-2f13-4b5b-bd38-383be2d15a40','admin@admin',
     'admin', '$2a$12$inf0sckTMZ2O5evP5OwH4OKM0Te16xHP6SV.H8fgYVaXYcrlnoBQO',
     'admin', 'ADMIN', 0, true),
    ('a040df1a-caca-4d5c-bc4e-73b394d30e96','2@gmail.com',
     'Mickael', '$2a$12$duDVhOiKqyA638OPugXXs.lCgwNZjqWfWQttLmsAqdV1sAfcwpOny',
     '+78887776655', 'USER', 0, true),
    ('6386a1e7-811b-405c-ae91-ea6478ac78ca','3@gmail.com',
     'Vladislav', '$2a$10$oBo92W0z91c6EtfqvxEpUOJ/OP0XdiXX7UMPsihED3Y4.gRuqeVbS',
     '+77776665544', 'USER', 0, true),
    ('f80007c4-b082-4e45-86e4-64491bd5767f','4@gmail.com',
     'Alexander', '$2a$12$7GIMHXbD1MbTrWVoDO5zSOKqY2ysoeGNZkNq5UAvpK43rB5XN5nUG',
     '+76665554433', 'USER', 0, true),
    ('9fc599b2-8984-4e42-91fc-1b1999c7eaba','5@gmail.com',
     'Tykhon', '$2a$12$nbhGwR2HO0ySzjz7MTdcmerbdLvDkhvQvbFO7g4/EM1oEFJlY7Spy',
     '+75554443322', 'USER', 0, true)
    ON CONFLICT(id)
DO UPDATE SET actual = TRUE;
