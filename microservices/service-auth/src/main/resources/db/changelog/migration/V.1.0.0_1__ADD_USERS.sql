INSERT INTO users (id, email, name, password, phone, role, version, actual)
VALUES
    ('3be29447-f8f3-4fb9-b5ca-218f3b08f417','admin@admin',
     'admin', '$2a$12$inf0sckTMZ2O5evP5OwH4OKM0Te16xHP6SV.H8fgYVaXYcrlnoBQO',
     'admin', 'ADMIN', 0, true),
    ('bf5f8924-bbc8-4eea-a934-17dfbb524816','2@gmail.com',
     'Mickael', '$2a$12$duDVhOiKqyA638OPugXXs.lCgwNZjqWfWQttLmsAqdV1sAfcwpOny',
     '+78887776655', 'USER', 0, true),
    ('3fa4a848-bd31-4083-994f-fc6f99f3e303','3@gmail.com',
     'Vladislav', '$2a$10$oBo92W0z91c6EtfqvxEpUOJ/OP0XdiXX7UMPsihED3Y4.gRuqeVbS',
     '+77776665544', 'USER', 0, true),
    ('bbcb83e5-e4fd-47ae-949b-a07ab9707db3','4@gmail.com',
     'Alexander', '$2a$12$7GIMHXbD1MbTrWVoDO5zSOKqY2ysoeGNZkNq5UAvpK43rB5XN5nUG',
     '+76665554433', 'USER', 0, true),
    ('b059201d-730d-4b1c-8a72-afb6206fd729','5@gmail.com',
     'Tykhon', '$2a$12$nbhGwR2HO0ySzjz7MTdcmerbdLvDkhvQvbFO7g4/EM1oEFJlY7Spy',
     '+75554443322', 'USER', 0, true)
    ON CONFLICT(id)
DO UPDATE SET actual = TRUE;
