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
END
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
values ('Forgot password', 'Software', 
		'Won''t load at all', 'Pls. access MyUON portal for password reset', 
		'Able to reset password now', '2022-04-26 10:26:30 AM', 'Resolved');

Insert into IssueDtls (IssueDescription, CategoryName,SubcategoryName, ITComment, UserComment, DateResolved, IssueStatus)
values ('unable to login', 'Network', 
		'Can''t connect', 'Check your network connection', 
		'Now able to connect', '2022-04-1 10:26:30 AM', 'Resolved');
		
CREATE VIEW [LAST7DAYS] AS SELECT * FROM IssueDtls WHERE DateResolved >= DATEADD(day,-7, GETDATE())