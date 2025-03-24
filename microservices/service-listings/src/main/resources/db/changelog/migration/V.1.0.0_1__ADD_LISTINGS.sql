INSERT INTO listings (id, car_id, user_id, created_at, expires_at, status, version, actual)
VALUES
    ('6c4b4284-7167-4e89-a80d-6164a0adf982', '43ef2468-2f13-4b5b-bd38-383be2d15a40',
     '3be29447-f8f3-4fb9-b5ca-218f3b08f417',NOW(),
     NOW() + INTERVAL '1 month','AVAILABLE',0, true),
    ('1109d413-fc6b-405f-95aa-a47e418965a0', 'a040df1a-caca-4d5c-bc4e-73b394d30e96',
     'bf5f8924-bbc8-4eea-a934-17dfbb524816',NOW(),
     NOW() + INTERVAL '1 month','AVAILABLE',0, true),
    ('f2a7fb7a-22f9-4ae7-adc4-b241ea7cad19', '6386a1e7-811b-405c-ae91-ea6478ac78ca',
     '3fa4a848-bd31-4083-994f-fc6f99f3e303',NOW(),
     NOW() + INTERVAL '1 month','AVAILABLE',0, true),
    ('c2eb9728-688f-44f4-ae85-5ee968b13891', 'f80007c4-b082-4e45-86e4-64491bd5767f',
     'bbcb83e5-e4fd-47ae-949b-a07ab9707db3',NOW(),
     NOW() + INTERVAL '1 month','AVAILABLE',0, true),
    ('19420da7-7326-4138-95a8-9870cd549271', '9fc599b2-8984-4e42-91fc-1b1999c7eaba',
     'b059201d-730d-4b1c-8a72-afb6206fd729',NOW(),
     NOW() + INTERVAL '1 month','AVAILABLE',0, true)
    ON CONFLICT(id)
DO UPDATE SET actual = TRUE;
