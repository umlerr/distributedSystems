INSERT INTO cars (id, vin, brand, model, price, year, mileage, version, actual)
VALUES
    ('43ef2468-2f13-4b5b-bd38-383be2d15a40', '1YVUB99879F60WMZZ','BMW',
     'M4 CS', 13000000,2018,10000, 0, true),
    ('a040df1a-caca-4d5c-bc4e-73b394d30e96', 'KNMESH62VB2H0LSAH','BMW',
     '320i', 3500000,2016,150000, 0, true),
    ('6386a1e7-811b-405c-ae91-ea6478ac78ca', '3H73BZB9GVP9L3217','Lada',
     '2106', 120000,1986,60000, 0, true),
    ('f80007c4-b082-4e45-86e4-64491bd5767f', 'JA3R6M1EZJ4CPS0TF','Subaru',
     'BRZ', 2500000,2017,65000, 0, true),
    ('9fc599b2-8984-4e42-91fc-1b1999c7eaba', '2FZ6E05JTFH72TYKU','Mazda',
     'MX-5', 4000000,2005,30000, 0, true)
    ON CONFLICT(id)
DO UPDATE SET actual = TRUE;
