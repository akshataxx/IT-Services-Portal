USE [master];
GO

IF DB_ID (N'seng2050') IS NOT NULL
DROP DATABASE seng2050;
GO
CREATE DATABASE seng2050;
GO

USE seng2050;
GO

IF SUSER_ID(N'jdbcUserSeng2050') IS NOT NULL
BEGIN
	DROP LOGIN jdbcUserseng2050;
END
CREATE LOGIN jdbcUserseng2050 WITH PASSWORD='mypassword', DEFAULT_DATABASE=seng2050;
GO

IF USER_ID(N'jdbcUserseng2050') IS NOT NULL
BEGIN
	DROP USER jdbcUserseng2050;
END
CREATE USER jdbcUserseng2050 FOR LOGIN jdbcUserseng2050;
GRANT SELECT, INSERT, UPDATE, DELETE TO jdbcUserseng2050;
GO

IF OBJECT_ID(N'user', N'U') IS NOT NULL
BEGIN
	DROP TABLE userdtls

	Create Table userdtls (
			username VARCHAR(20) NOT NULL, 
                        passwrd VARCHAR(20) NOT NULL,
                        fname VARCHAR(20) NOT NULL,
                        surname VARCHAR(20) NOT NULL,
                        email VARCHAR(20) NOT NULL,
                        contactno INT NOT NULL,
                        userrole VARCHAR(20) NOT NULL,
			PRIMARY KEY (fname , surname )
                    );

	Insert into userdtls values
			('Akshi', 'Akshi', 'Akshata', 'Dhuraji', 'akshata@email.com', 111222333, 'Student'),
			('Reena', 'Reena', 'Reena', 'Thomas', 'reena@email.com', 444333222, 'Student'),       
			('Sam', 'Sam', 'Sam', 'Dsouza', 'sam@email.com', 666555111, 'Student'),
			('Nova', 'Nova', 'Nova', 'Johnson', 'nova@email.com', 888999444, 'IT-Staff'),
			('Mita', 'Mita', 'Mita', 'Dixit', 'mita@email.com', 232323232, 'IT-Staff');

	DROP TABLE IssueDtls;
	Create Table IssueDtls ( 
				IssueId INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
				IssueDescription VARCHAR (50) NOT NULL,
				CategoryName VARCHAR (20) NOT NULL,
				SubcategoryName VARCHAR(20) NOT NULL,
				DateRaised date not null default convert(date,getdate()),
				ITComment Varchar(100) ,
				UserComment Varchar(100) ,
				DateResolved DATETIME,
				IssueStatus  VARCHAR(20) not null 
	);
					 
	Insert into IssueDtls (IssueDescription, CategoryName,SubcategoryName, ITComment, UserComment, DateResolved, IssueStatus)
	values ('Forgot password', 'Software', 'Won''t load at all', 'Pls. access MyUON portal for password reset',  
			'Able to reset password now', '2022-04-26 10:26:30 AM', 'Resolved');
			('unable to login', 'Network','Can''t connect', 'Check your network connection', 
			'Now able to connect', '2022-04-1 10:26:30 AM', 'Resolved');
			('unable to login', 'Network', 'Can''t connect', 'Check your network connection', 
			'Now able to connect', '2022-04-26 10:26:30 AM', 'InProgress');
			('unable to login', 'Network', 'Can''t connect', 'Check your network connection', 
			'Now able to connect', '2022-04-26 10:26:30 AM', 'New');		
		
	DROP VIEW [LAST7DAYS];
	CREATE VIEW [LAST7DAYS] AS 
		SELECT * FROM IssueDtls 
		WHERE DateResolved >= DATEADD(day,-7, GETDATE()) 
		ORDER BY [CategoryName] OFFSET 0 ROWS; 

	DROP VIEW [UNRESOLVEDINCIDENTS];
	CREATE VIEW [UNRESOLVEDINCIDENTS] AS 
		SELECT * FROM IssueDtls
		WHERE IssueStatus <> 'Resolved';

	Drop TABLE ITSTAFF;
	CREATE TABLE ITSTAFF (StaffNo INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
					 Fname VARCHAR(20) NOT NULL, 
					 Lname VARCHAR(20) NOT NULL,
					 EmailId VARCHAR(30) NOT NULL,
					 PhoneNo INT NOT NULL,
					 DateOfJoining Date NOT NULL,
					 DateOfLeaving Date,
					 );

	Insert into ITSTAFF(Fname,Lname,EmailId,PhoneNo, DateOfJoining) 
	values 	('Herman', 'Dsouza', 'herman.dsouza@gmail.com',13131313, '2018-01-29')
			('Akshata','Dhuri','akshi.dhuri@gmail.com',12121212, '2015-12-17');
	
	Insert into ITSTAFF(Fname,Lname,EmailId,PhoneNo, DateOfJoining, DateOfLeaving) values ('Meena', 'Sharma', 'meena.sharma@gmail.com',21212121, '2018-01-29', '2020-03-15');
END	

CREATE PROCEDURE StressReport
@StressRate DECIMAL(9,2) OUTPUT
AS
BEGIN
	DECLARE @NUMOFITSTAFF INT = (SELECT COUNT(*) FROM ITSTAFF WHERE DateOfLeaving IS NULL)
	DECLARE @NUMOFUNRESOLVEDISSUE INT =(SELECT COUNT(*) FROM IssueDtls WHERE IssueStatus <> 'Resolved')
	SELECT @StressRate= (@NUMOFUNRESOLVEDISSUE/ (@NUMOFITSTAFF  * 5));
	RETURN;
END


