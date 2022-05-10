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
                       surname 		    VARCHAR(35) NOT NULL,
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
                       description		VARCHAR(2000) 	NOT NULL,
                       title			VARCHAR(150) 	NOT NULL,
                       mainCategory	    VARCHAR(20)	    NOT NULL,
                       subCategory		VARCHAR(20)		NOT NULL,
                       dateReported 	BIGINT			NOT NULL,
                       knowledgeBase	BIT				NOT	NULL,
                       issueStatus		VARCHAR(20)		NOT NULL,
                       dateResolved 	BIGINT,
                       reporter		    VARCHAR(36),
                       CONSTRAINT pkIssue 		PRIMARY KEY(uniqueId),
                       CONSTRAINT IssuefkReporter 	FOREIGN KEY (reporter) REFERENCES Users(uniqueId) ON UPDATE CASCADE ON DELETE SET NULL
);

INSERT INTO Issue (uniqueId,description,title,mainCategory,subCategory,dateReported,knowledgeBase,issueStatus,reporter)
VALUES (0,'Pls. access MyUON portal for password reset','Forgot password','ACCOUNT','PASSWORD_RESET',1652006984560,0,'COMPLETED','dc7bfbf0-cc69-11ec-9d64-0242ac120002');

INSERT INTO Issue (uniqueId,description,title,mainCategory,subCategory,dateReported,knowledgeBase,issueStatus,reporter)
VALUES (1,'Unable to log on','help','NETWORK','CONSTANT_DROPOUTS',1652163305130,0,'NEW','f3820a9c-cc69-11ec-9d64-0242ac120002');

INSERT INTO Issue (uniqueId,description,title,mainCategory,subCategory,dateReported,knowledgeBase,issueStatus,reporter)
VALUES (2,'New computer wont turn on','new computer bad','HARDWARE','COMP_WONT_TURN_ON',1652163279943,0,'NEW','f3820a9c-cc69-11ec-9d64-0242ac120002');

INSERT INTO Issue (uniqueId,description,title,mainCategory,subCategory,dateReported,knowledgeBase,issueStatus,dateResolved,reporter)
VALUES (3,'Hello. I was using my computer which you guys gave me and on the 4th of 2022 my computer all of a sudden broke and wont turn on. idk if I got a virus','Old Laptop Wont Turn on','HARDWARE','COMP_WONT_TURN_ON',1652160634689,1,'RESOLVED',1652162084093,'dc7bfbf0-cc69-11ec-9d64-0242ac120002');

INSERT INTO Issue (uniqueId,description,title,mainCategory,subCategory,dateReported,knowledgeBase,issueStatus,reporter)
VALUES (4,'Hello, I was looking at very bad websites on my computer and then it suddenly blue screeened :(','Computer BSOD while using internet','HARDWARE','COMP_BLUE_SCREEN',1652164952257,0,'IN_PROGRESS','f029bb1a-cc69-11ec-9d64-0242ac120002');
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
INSERT INTO Comments VALUES('220b6a25-d166-4544-bc49-134cd9550928','please give answer',43489539453,'dc7bfbf0-cc69-11ec-9d64-0242ac120002',0);
INSERT INTO Comments VALUES('601bfa7f-1a08-4a59-9827-5c4fc4f05664','what brand of computer do you have',1652161083182,'f9d5d4dc-cc69-11ec-9d64-0242ac120002',3);
INSERT INTO Comments VALUES('c7cfbeed-ef11-4adb-985b-204538c6ae34','Dell Super',1652161204009,'dc7bfbf0-cc69-11ec-9d64-0242ac120002',3);
INSERT INTO Comments VALUES('9156a55e-314c-4c2e-9b6a-155b93f23506','Does the charging light turn on?',1652161859989,'fc456b24-cc69-11ec-9d64-0242ac120002',3);
INSERT INTO Comments VALUES('12105e4a-9540-49dc-bfef-227f8a9d9585','No sir',1652161899113,'dc7bfbf0-cc69-11ec-9d64-0242ac120002',3);

--SELECT * FROM Comments;

go

go

CREATE TABLE Solutions (
                           id			VARCHAR(36)			NOT NULL,
                           details		VARCHAR(2000)		NOT NULL,
                           postTime	BIGINT				    NOT NULL,
                           status		VARCHAR(10)			NOT NULL,
                           staff		VARCHAR(36),
                           issue		INT,

                           CONSTRAINT pkSolutions	PRIMARY KEY(id),
                           CONSTRAINT SolutionsfkStaff	FOREIGN KEY (staff) REFERENCES Users(uniqueId) ON UPDATE CASCADE ON DELETE SET NULL,
                           CONSTRAINT SolutionsfkIssue		FOREIGN KEY (issue)	REFERENCES Issue(uniqueId) ON UPDATE NO ACTION ON DELETE CASCADE,
);

INSERT INTO Solutions VALUES('64dcd084-9058-4d0c-9134-d39fb94d1c96','Have you tried resetting your password using myUON?',494383895,'WAITING','fc456b24-cc69-11ec-9d64-0242ac120002',0);
INSERT INTO Solutions VALUES('4ca1354e-3180-428f-954f-94dfbc4629de','Plugin in your computer with a universal charging adapter',1652161478664,'REJECTED','fc456b24-cc69-11ec-9d64-0242ac120002',3);
INSERT INTO Solutions VALUES('4fa98e14-31a5-4832-a79f-cba4e44b685c','Take your computer into the shop at IT shop 321 example street at 3PM 10.5.2022, the battery is dead and it needs replacing!',1652162519275,'ACCEPTED','fc456b24-cc69-11ec-9d64-0242ac120002',3);


--SELECT * FROM Solutions;

go

CREATE TABLE Notifications (
                               id			VARCHAR(36)		NOT NULL,
                               title		VARCHAR(100)	NOT NULL,
                               content		VARCHAR(350)	NOT NULL,
                               sendTime    	BIGINT,
                               person		VARCHAR(36),
                               issue		INT,


                               CONSTRAINT pkNotifications	PRIMARY KEY(id),
                               CONSTRAINT NotificationsfkPerson	FOREIGN KEY (person) REFERENCES Users(uniqueId) ON UPDATE CASCADE ON DELETE CASCADE,
                               CONSTRAINT NotificationsfkIssue		FOREIGN KEY (issue)	REFERENCES Issue(uniqueId) ON UPDATE NO ACTION ON DELETE SET NULL,
);

INSERT INTO Notifications VALUES('95fdcc44-a368-4fc8-aa8a-3bf6b14dbeab','check quick','it is very important',1652011700274,'f9d5d4dc-cc69-11ec-9d64-0242ac120002',0);
INSERT INTO Notifications VALUES('dc7bfbf0-cc69-11ec-9d64-0242ac120002','you have solution','check solution',1652011700405,'dc7bfbf0-cc69-11ec-9d64-0242ac120002',0)
INSERT INTO Notifications VALUES('855ec8ed-9d0d-4c0b-9e81-ba4f09842a6d','Waiting on You','A staff member is awaiting your assistance to solve your issue #3',1652161796914,'dc7bfbf0-cc69-11ec-9d64-0242ac120002',3);
INSERT INTO Notifications VALUES('48b2a731-1d56-4af3-9738-584bf2c7ee26','New Solution','A solution has been added to your issue #3',1652161823100,'dc7bfbf0-cc69-11ec-9d64-0242ac120002',3);
INSERT INTO Notifications VALUES('ca7feccd-2e6a-4eab-8749-74851dcd516e','Solution Rejected','Your solution for issue #3 has been rejected by the user!',1652161834296,'fc456b24-cc69-11ec-9d64-0242ac120002',3);

go

SELECT COUNT(*) AS issueCount FROM Issue

go