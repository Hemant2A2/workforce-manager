-- Create the database
CREATE DATABASE IF NOT EXISTS workforce_db;
USE workforce_db;

-- -----------------------------------------------------
-- Table `Member_types`
-- This table stores different types of member roles (e.g., manager, employee).
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Member_types (
  mem_typeId INT AUTO_INCREMENT,
  title ENUM("ADMIN", "MANAGER", "MEMBER") NOT NULL,
  allowed_paid_leave INT NOT NULL DEFAULT 5,
  allowed_hours INT NOT NULL DEFAULT 60,
  PRIMARY KEY (mem_typeId)
);

-- -----------------------------------------------------
-- Table `Location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Location (
  location_Id INT AUTO_INCREMENT,
  manager_Id INT NOT NULL, 
  plot_No VARCHAR(45) NULL,
  Street VARCHAR(255) NULL,
  city VARCHAR(255) NULL,
  pincode VARCHAR(10) NULL,
  PRIMARY KEY (location_Id)
);



-- -----------------------------------------------------
-- Table `Member`
-- Stores information about each company member.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Member (
  EmpId INT AUTO_INCREMENT,
  Fname VARCHAR(255) NOT NULL,
  Mname VARCHAR(255) NOT NULL DEFAULT "",
  Lname VARCHAR(255) NOT NULL DEFAULT "",
  Gender ENUM("MALE" ,  "FEMALE" , "OTHERS") NULL,
  Apartment VARCHAR(255) NULL,
  city VARCHAR(255) NULL,
  DOB DATE NULL,
  Password VARCHAR(255),
  Overtime_required DECIMAL(10,2),
  phone VARCHAR(20) NULL,
  availed_leaves INT NULL DEFAULT 0,
  locationId INT,
  mem_typeId INT,
  PRIMARY KEY (EmpId),
  CONSTRAINT fk_Member_Location1
    FOREIGN KEY (locationId)
    REFERENCES Location(location_Id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Member_Member_types1
    FOREIGN KEY (mem_typeId)
    REFERENCES Member_types(mem_typeId)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ;

-- location manager constraint
ALTER TABLE Location
ADD FOREIGN KEY (manager_Id) REFERENCES Member(EmpID)     
ON DELETE NO ACTION
ON UPDATE NO ACTION;

-- -----------------------------------------------------
-- Table `Roles`
-- Defines different roles with their pay rates.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Roles (
  roleId INT AUTO_INCREMENT,
  role_name VARCHAR(255) NOT NULL,
  Standard_Rate DECIMAL(10, 2) NOT NULL,
  Overtime_Rate DECIMAL(10, 2) NULL,
  PRIMARY KEY (roleId)
);

-- -----------------------------------------------------
-- Table `Notification`
-- Stores notifications sent to members.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Notification (
  notif_id INT AUTO_INCREMENT,
  MemberId INT NOT NULL,
  timestamps TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  View_Time TIMESTAMP NULL,
  message TEXT NOT NULL,
  title VARCHAR(255) NULL,
  PRIMARY KEY (notif_id, MemberId),
  CONSTRAINT fk_Notification_Member1
    FOREIGN KEY (MemberId)
    REFERENCES Member(EmpId)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ;

-- -----------------------------------------------------
-- Table `Feasible_Role`
-- A junction table for many-to-many relationship between `Member` and `Roles`.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Feasible_Role (
  mem_Id INT NOT NULL,
  role_Id INT NOT NULL,
  PRIMARY KEY (mem_Id, role_Id),
  CONSTRAINT fk_Feasible_Role_Member1
    FOREIGN KEY (mem_Id)
    REFERENCES Member(EmpId)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Feasible_Role_Roles1
    FOREIGN KEY (role_Id)
    REFERENCES Roles(roleId)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ;

-- -----------------------------------------------------
-- Table `Week`
-- A simple table to track weeks, likely for payment periods.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Week (
  weekId INT AUTO_INCREMENT,
  start_date DATE NOT NULL,
  PRIMARY KEY (weekId)
) ;

-- -----------------------------------------------------
-- Table `Payment`
-- Tracks payments for members on a weekly basis.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Payment (
  weekId INT NOT NULL,
  memb_Id INT NOT NULL,
  Amount DECIMAL(10, 2) NOT NULL,
  unapproved_leave INT NULL DEFAULT 0,
  PRIMARY KEY (memb_Id, weekId),
  CONSTRAINT fk_Payment_Week1
    FOREIGN KEY (weekId)
    REFERENCES Week(weekId)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Payment_Member1
    FOREIGN KEY (memb_Id)
    REFERENCES Member(EmpId)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table `Shift`
-- Defines a specific work shift.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Shift (
  Shift_Id INT AUTO_INCREMENT,
  Day VARCHAR(20) NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  title VARCHAR(255) NULL,
  location_Id INT,
  PRIMARY KEY (Shift_Id),
  CONSTRAINT fk_Shift_Location1
    FOREIGN KEY (location_Id)
    REFERENCES Location(location_Id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);
-- -----------------------------------------------------
-- Table `Shift_Assignment`
-- Assigns members to specific shifts and roles for a week.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Shift_Assignment(
  ShiftId INT NOT NULL,
  EmpId INT NOT NULL,
  Week_Id INT NOT NULL,
  Role_Id INT NOT NULL,
  Attendance ENUM("PRESENT", "ABSENT", "LEAVE", "SICK") NULL,
  PRIMARY KEY (ShiftId, Week_Id , EmpId, Role_Id),
  CONSTRAINT fk_Shift_Assignment_Shift1
    FOREIGN KEY (ShiftId)
    REFERENCES Shift(Shift_Id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Shift_Assignment_Member1
    FOREIGN KEY (EmpId)
    REFERENCES Member(EmpId)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Shift_Assignment_Week1
    FOREIGN KEY (Week_Id)
    REFERENCES Week(weekId)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Shift_Assignment_Roles1
    FOREIGN KEY (Role_Id)
    REFERENCES Roles(roleId)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table `Requirement`
-- Tracks the number of people needed for a specific role and shift.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Requirement(
  role_Id INT NOT NULL,
  Shift_Id INT NOT NULL,
  count INT NOT NULL,
  PRIMARY KEY (role_Id, Shift_Id),
  CONSTRAINT fk_Requirement_Roles1
    FOREIGN KEY (role_Id)
    REFERENCES Roles(roleId)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Requirement_Shift1
    FOREIGN KEY (Shift_Id)
    REFERENCES Shift(Shift_Id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ;

-- -----------------------------------------------------
-- Table `Template`
-- A template for a set of shifts.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Template (
  templateId INT AUTO_INCREMENT,
  created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  description TEXT NULL,
  creator INT NOT NULL,
  PRIMARY KEY (templateId),
  CONSTRAINT fk_Template_Member1
    FOREIGN KEY (creator)
    REFERENCES Member(EmpId)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ;

-- -----------------------------------------------------
-- Table `TempItems`
-- Links shifts and roles to a template.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS TempItems (
  Empid INT NOT NULL,
  ShiftId INT NOT NULL,
  roleId INT NOT NULL,
  TemplateId INT NOT NULL,
  PRIMARY KEY (ShiftId, TemplateId, Empid, roleId),
  CONSTRAINT fk_TempItems_Member1
    FOREIGN KEY (Empid)
    REFERENCES Member(EmpId)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_TempItems_Shift1
    FOREIGN KEY (ShiftId)
    REFERENCES Shift(Shift_Id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_TempItems_Roles1
    FOREIGN KEY (roleId)
    REFERENCES Roles(roleId)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_TempItems_Template1
    FOREIGN KEY (TemplateId)
    REFERENCES Template(templateId)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ;

-- -----------------------------------------------------
-- Table `Unavailability`
-- Tracks when a member is unavailable for a shift.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Unavailability (
  EmpId INT NOT NULL,
  ShiftId INT NOT NULL,
  Approval BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (EmpId, ShiftId),
  CONSTRAINT fk_Unavailability_Member1
    FOREIGN KEY (EmpId)
    REFERENCES Member(EmpId)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Unavailability_Shift1
    FOREIGN KEY (ShiftId)
    REFERENCES Shift(Shift_Id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ;

-- -----------------------------------------------------
-- Table `Leave_Request`
-- Manages leave requests for specific shifts.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Leave_Request(
  EmpId INT NOT NULL,
  ShiftId INT NOT NULL,
  Approval ENUM("PENDING", "APPROVED", "REJECTED") NOT NULL DEFAULT "PENDING",
  PRIMARY KEY (EmpId, ShiftId),
  CONSTRAINT fk_Leave_Request_Member1
    FOREIGN KEY (EmpId)
    REFERENCES Member(EmpId)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Leave_Request_Shift1
    FOREIGN KEY (ShiftId)
    REFERENCES Shift(Shift_Id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ;
