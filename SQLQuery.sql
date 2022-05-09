USE [master];
GO

-- DROP DATABASE - remove on final product
DROP DATABASE seng2050;
CREATE DATABASE seng2050;

USE seng2050;

go
-- DROP LOGINS - remove on final product
DROP LOGIN jdbcUserseng2050;

--- Create Logins ---
CREATE LOGIN jdbcUserseng2050 WITH PASSWORD='mypassword', DEFAULT_DATABASE=seng2050;
GO

DROP USER jdbcUserseng2050;
--- Create users ---
CREATE USER jdbcUserseng2050 FOR LOGIN jdbcUserseng2050;
GRANT SELECT, INSERT, UPDATE, DELETE TO jdbcUserseng2050;
GO

--- I understand using strings as primary key isn't good pracitce but considering the scale
--- of the system the slowdown will be miniscile

--DROP TABLES - remove on final product
go
DROP TABLE Users;
DROP TABLE Issue;
DROP TABLE Comments;
DROP TABLE Solutions;
DROP TABLE Notifications;
go
---- CREATE TABLES ----
CREATE TABLE Users (
                       uniqueid   		VARCHAR(36)	NOT NULL,
                       username 		VARCHAR(20) NOT NULL,
                       password 		VARCHAR(20) NOT NULL,
                       first_name 		VARCHAR(35) NOT NULL,
                       surname 		VARCHAR(35) NOT NULL,
                       email 			VARCHAR(320),
                       contactno 		VARCHAR(15),
                       role			VARCHAR(10),
                       CONSTRAINT pkUser PRIMARY KEY(uniqueId),
);

INSERT INTO Users values
                      ('dc7bfbf0-cc69-11ec-9d64-0242ac120002','Akshi', 'Akshi', 'Akshata', 'Dhuraji', 'akshata@email.com', '111222333', 'USER'),
                      ('f029bb1a-cc69-11ec-9d64-0242ac120002','Reena', 'Reena', 'Reena', 'Thomas', 'reena@email.com', '444333222', 'USER'),
                      ('f3820a9c-cc69-11ec-9d64-0242ac120002','Sam', 'Sam', 'Sam', 'Dsouza', 'sam@email.com', '666555111', 'USER'),
                      ('f9d5d4dc-cc69-11ec-9d64-0242ac120002','Nova', 'Nova', 'Nova', 'Johnson', 'nova@email.com', '888999444', 'IT_STAFF'),
                      ('fc456b24-cc69-11ec-9d64-0242ac120002','Mita', 'Mita', 'Mita', 'Dixit', 'mita@email.com', '232323232', 'IT_STAFF');

--SELECT * FROM Users;
go

go
CREATE TABLE Issue (
                       uniqueId 		INT				NOT NULL,
                       description		VARCHAR (500) 	NOT NULL,
                       title			VARCHAR(20) 	NOT NULL,
                       mainCategory	VARCHAR(20)		    NOT NULL,
                       subCategory		VARCHAR(20)		NOT NULL,
                       dateReported 	BIGINT			NOT NULL,
                       knowledgeBase	BIT				NOT	NULL,
                       issueStatus		VARCHAR(20)		NOT NULL,
                       dateResolved 	BIGINT,
                       reporter		VARCHAR(36),
                       CONSTRAINT pkIssue 		PRIMARY KEY(uniqueId),
                       CONSTRAINT IssuefkReporter 	FOREIGN KEY (reporter) REFERENCES Users(uniqueId) ON UPDATE CASCADE ON DELETE SET NULL
);

INSERT INTO Issue (uniqueId,description,title,mainCategory,subCategory,dateReported,knowledgeBase,issueStatus,reporter)
VALUES (0,'Pls. access MyUON portal for password reset','Forgot password','ACCOUNT','PASSWORD_RESET',1652006984560,0,'NEW','dc7bfbf0-cc69-11ec-9d64-0242ac120002');

INSERT INTO Issue (uniqueId,description,title,mainCategory,subCategory,dateReported,knowledgeBase,issueStatus,reporter)
VALUES (1,'Unable to log on','help','NETWORK','CONSTANT_DROPOUTS',19034043345,1,'RESOLVED','f3820a9c-cc69-11ec-9d64-0242ac120002');

--SELECT * FROM Issue;

go

go

CREATE TABLE Comments (
                          id			VARCHAR(36)			NOT NULL,
                          comment		VARCHAR(500)		NOT NULL,
                          timePosted	BIGINT				NOT NULL,
                          commenter	    VARCHAR(36),
                          issue		    INT,

                          CONSTRAINT pkComment	PRIMARY KEY(id),
                          CONSTRAINT CommentfkCommenter	FOREIGN KEY (commenter) REFERENCES Users(uniqueId) ON UPDATE CASCADE ON DELETE SET NULL,
                          CONSTRAINT CommentfkIssue		FOREIGN KEY (issue)	REFERENCES Issue(uniqueId) ON UPDATE NO ACTION ON DELETE CASCADE,
);

INSERT INTO Comments VALUES('02132211-b5f6-457d-9b77-ff91e114b58a','hello this is tech support',4390583409,'f9d5d4dc-cc69-11ec-9d64-0242ac120002',0);
INSERT INTO Comments VALUES('220b6a25-d166-4544-bc49-134cd9550928','please give answer',43489539453,'f3820a9c-cc69-11ec-9d64-0242ac120002',0);

--SELECT * FROM Comments;

go

go

CREATE TABLE Solutions (
                           id			VARCHAR(36)			NOT NULL,
                           details		VARCHAR(500)		NOT NULL,
                           postTime	BIGINT				NOT NULL,
                           status		VARCHAR(10)			NOT NULL,
                           staff		VARCHAR(36),
                           issue		INT,

                           CONSTRAINT pkSolutions	PRIMARY KEY(id),
                           CONSTRAINT SolutionsfkStaff	FOREIGN KEY (staff) REFERENCES Users(uniqueId) ON UPDATE CASCADE ON DELETE SET NULL,
                           CONSTRAINT SolutionsfkIssue		FOREIGN KEY (issue)	REFERENCES Issue(uniqueId) ON UPDATE NO ACTION ON DELETE CASCADE,
);

INSERT INTO Solutions VALUES('64dcd084-9058-4d0c-9134-d39fb94d1c96','get good',494383895,'WAITING','fc456b24-cc69-11ec-9d64-0242ac120002',0);

--SELECT * FROM Solutions;

go

CREATE TABLE Notifications (
                               id			VARCHAR(36)		NOT NULL,
                               title		VARCHAR(50)		NOT NULL,
                               content		VARCHAR(500)	NOT NULL,
                               sendTime    	BIGINT,
                               person		VARCHAR(36),
                               issue		INT,


                               CONSTRAINT pkNotifications	PRIMARY KEY(id),
                               CONSTRAINT NotificationsfkPerson	FOREIGN KEY (person) REFERENCES Users(uniqueId) ON UPDATE CASCADE ON DELETE CASCADE,
                               CONSTRAINT NotificationsfkIssue		FOREIGN KEY (issue)	REFERENCES Issue(uniqueId) ON UPDATE NO ACTION ON DELETE SET NULL,
);

INSERT INTO Notifications VALUES('95fdcc44-a368-4fc8-aa8a-3bf6b14dbeab','check quick','it is very important',1652011700274,'f9d5d4dc-cc69-11ec-9d64-0242ac120002',0);
INSERT INTO Notifications VALUES('dc7bfbf0-cc69-11ec-9d64-0242ac120002','you have solution','check solution',1652011700405,'dc7bfbf0-cc69-11ec-9d64-0242ac120002',0)

go

SELECT COUNT(*) AS issueCount FROM Issue

go