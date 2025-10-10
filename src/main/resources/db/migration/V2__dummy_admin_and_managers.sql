USE workforce_db;

INSERT INTO `Member_Types` (`title`, `allowed_hours`, `allowed_paid_leave`) VALUES
  ('ADMIN',   160, 30),
  ('MANAGER', 160, 24);

INSERT INTO `Member`
  (`Fname`,`Mname`,`Lname`,`Gender`,`Apartment`,`city`,`DOB`,`Password`,
   `Overtime_required`,`phone`,`availed_leaves`,`locationId`,`mem_typeId`)
VALUES
  ('Alice','A','Admin','FEMALE','Apt 1','Mumbai','1985-01-01','password',
   0.0,'9000000001',0, NULL, (SELECT mem_typeId FROM `Member_Types` WHERE title = 'ADMIN'));

INSERT INTO `Member`
  (`Fname`,`Mname`,`Lname`,`Gender`,`Apartment`,`city`,`DOB`,`Password`,
   `Overtime_required`,`phone`,`availed_leaves`,`locationId`,`mem_typeId`)
VALUES
  ('Bob','B','Manager','MALE','Apt 2','Mumbai','1987-02-10','password',0.0,'9000000011',0, NULL, (SELECT mem_typeId FROM `Member_Types` WHERE title = 'MANAGER')),
  ('Charlie','C','Manager','MALE','Apt 3','Pune','1986-03-12','password',0.0,'9000000012',0, NULL, (SELECT mem_typeId FROM `Member_Types` WHERE title = 'MANAGER')),
  ('David','D','Manager','MALE','Apt 4','Bengaluru','1988-04-20','password',0.0,'9000000013',0, NULL, (SELECT mem_typeId FROM `Member_Types` WHERE title = 'MANAGER')),
  ('Eve','E','Manager','FEMALE','Apt 5','Chennai','1990-05-30','password',0.0,'9000000014',0, NULL, (SELECT mem_typeId FROM `Member_Types` WHERE title = 'MANAGER')),
  ('Frank','F','Manager','MALE','Apt 6','Kolkata','1989-06-15','password',0.0,'9000000015',0, NULL, (SELECT mem_typeId FROM `Member_Types` WHERE title = 'MANAGER'));