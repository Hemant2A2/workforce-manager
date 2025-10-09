USE workforce_db;

INSERT INTO `Location` (`plot_No`, `Street`, `city`, `pincode`, `manager_Id`) VALUES
  ('12A',  'Maple Street',     'Mumbai',     '400001', 2),
  ('7B',   'Oak Avenue',       'Pune',       '411001', 6),
  ('101',  'Cedar Lane',       'Bengaluru',  '560001', 5),
  ('45',   'Pine Road',        'Chennai',    '600001', 3),
  ('9C',   'Birch Boulevard',  'Kolkata',    '700001', 4);

UPDATE `Member` m
JOIN `Location` l ON l.manager_Id = m.EmpId
SET m.locationId = l.location_Id
WHERE m.locationId IS NULL;