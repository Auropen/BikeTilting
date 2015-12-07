USE master
IF EXISTS(select * from sys.databases where name='BikeTiltingDB')
DROP DATABASE BikeTiltingDB
go 

CREATE DATABASE BikeTiltingDB
go


USE BikeTiltingDB
go

--- Creating Tables
CREATE TABLE TblScore		(fldScoreID int IDENTITY(1,1) PRIMARY KEY,
							 fldHitScore VARCHAR (12) NOT NULL,
							 fldScore int NOT NULL)

CREATE TABLE TblLane		(fldLaneID int IDENTITY(1,1) PRIMARY KEY,
							 fldLaneNr int NOT NULL)

CREATE TABLE TblUsers		(fldCPR VARCHAR(12) PRIMARY KEY,
							 fldPassword VARCHAR (255)   NOT NULL,
							 fldFName VARCHAR(32)       NOT NULL,
							 fldLName VARCHAR(32)       NOT NULL,
							 fldEmail VARCHAR(255)       NOT NULL,
							 fldPhoneNumber VARCHAR(16),
							 fldAccessLevel INT         NOT NULL)


CREATE TABLE TblParticipants(fldParticipantID int IDENTITY(1,1) PRIMARY KEY,
							 fldFName VARCHAR(20)   NOT NULL,
							 fldLName VARCHAR(16)   NOT NULL,
							 fldAgeRange VARCHAR(6) NOT NULL,
							 fldEmail VARCHAR(64)   NOT NULL,
							 fldScoreID INT FOREIGN KEY REFERENCES TblScore (fldScoreID),
							 fldShirtColour VARCHAR(12),
							 fldShirtNumber int,  
							 fldLaneID INT FOREIGN KEY REFERENCES TblLane (fldLaneID))

CREATE TABLE TblVolunteer	(fldCPR VARCHAR(12) FOREIGN KEY REFERENCES TblUsers (fldCPR),
							 fldIsActive BINARY(1) NOT NULL)

GO

--- Stored Procedures

CREATE PROCEDURE createUser(@cpr VARCHAR(12), @password VARCHAR(255), @fName VARCHAR(32), @lName VARCHAR(32), @email VARCHAR(255), @phoneNumber VARCHAR(16), @accessLevel INT)
    AS 
	BEGIN
		INSERT INTO TblUsers
		(
			fldCPR,
			fldPassword,
			fldFName,
			fldLName,
			fldEmail,
		    fldPhoneNumber,
		    fldAccessLevel
		)
		VALUES
		(
			@Cpr,
			@password,
			@fName,
			@lName,
			@email,
			@phoneNumber,
			@accessLevel
		)
END

GO

CREATE PROCEDURE createParticipant(@fName VARCHAR(32), @lName VARCHAR(32), @ageRange VARCHAR(6), @email VARCHAR(255), @scoreID INT, @shirtColour VARCHAR(12), @shirtNumber INT, @laneID INT, @new_id INT OUTPUT)
    AS 
	BEGIN
		INSERT INTO TblParticipants
		(
			fldFName,
			fldLName,
			fldAgeRange,
			fldEmail,
		    fldScoreID,
		    fldShirtColour,
			fldShirtNumber,
			fldLaneID
		)
		VALUES
		(
			@fName,
			@lName,
			@ageRange,
			@email,
			@scoreID,
			@shirtColour,
			@shirtNumber,
			@laneID
		)
		SET @new_id = SCOPE_IDENTITY()
END
GO

CREATE PROCEDURE createScore(@hitScore VARCHAR(12), @score int, @new_id int output)
    AS 
	BEGIN
		INSERT INTO TblScore
		(
			fldHitScore,
			fldScore
		)
		VALUES
		(
			@hitScore,
			@score
		)
		SET @new_id = SCOPE_IDENTITY()
END
GO

--- Get procedures
CREATE PROCEDURE getParticipant(@id int)
    AS 
	BEGIN
		SELECT * FROM TblParticipants WHERE fldParticipantID = @id
		
END
GO

CREATE PROCEDURE getScoreByID(@id int)
    AS 
	BEGIN
		SELECT * FROM TblScore WHERE fldScoreID = @id;
		
END
GO

CREATE PROCEDURE getParcipantsByLaneID(@laneID int)
    AS 
	BEGIN
		SELECT * FROM TblParticipants WHERE fldLaneID = @laneID;
		
END
GO

---Update Procedures

CREATE PROCEDURE updateScorePoints(@id int,@hitScore VARCHAR(12), @score int)
    AS 
	BEGIN
		UPDATE TblScore SET fldHitScore = @hitScore, fldScore = @score WHERE fldScoreID= @id;

END
GO

--- Creating View's 

CREATE VIEW getUsers
    AS 
		SELECT * FROM TblUsers
GO

CREATE VIEW getLanes
    AS
		SELECT * FROM TblLane
GO