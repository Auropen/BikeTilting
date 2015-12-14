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
							 fldHitScore VARCHAR (255) NOT NULL,
							 fldScore int NOT NULL)

CREATE TABLE TblLanes		(fldLaneID int IDENTITY(1,1) PRIMARY KEY,
							 fldLaneNr int NOT NULL,
							 fldAgeGroup VARCHAR(6) NOT NULL)

CREATE TABLE TblParticipants(fldParticipantID int IDENTITY(1,1) PRIMARY KEY,
							 fldFName VARCHAR(20)   NOT NULL,
							 fldLName VARCHAR(16)   NOT NULL,
							 fldAgeRange VARCHAR(6) NOT NULL,
							 fldEmail VARCHAR(64)   NOT NULL,
							 fldScoreID INT FOREIGN KEY REFERENCES TblScore (fldScoreID),
							 fldShirtColour VARCHAR(12),
							 fldShirtNumber int,  
							 fldLaneID INT FOREIGN KEY REFERENCES TblLaneS (fldLaneID))

CREATE TABLE TblShirts		(fldColor VARCHAR(32) NOT NULL,
							 fldAmount int NOT NULL,
							 fldUsedColor BIT NOT NULL)

GO

--- Stored Procedures

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

CREATE PROCEDURE createLane(@laneNr INT, @ageGroup VARCHAR(6), @new_id INT OUTPUT)
    AS 
	BEGIN
		INSERT INTO TblLanes
		(
			fldLaneNr,
			fldAgeGroup
		)
		VALUES
		(
			@laneNr,
			@ageGroup
		)
		SET @new_id = SCOPE_IDENTITY()
END
GO

CREATE PROCEDURE createScore(@hitScore VARCHAR(255), @score int, @new_id int output)
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

CREATE PROCEDURE createColor(@color VARCHAR(32), @amount INT, @used BIT)
    AS 
	BEGIN
		INSERT INTO TblShirts
		(
			fldColor,
			fldAmount,
			fldUsedColor
		)
		VALUES
		(
			@color,
			@amount,
			@used
		)

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

CREATE PROCEDURE getParticipantsByLaneID(@laneID int)
    AS 
	BEGIN
		SELECT * FROM TblParticipants WHERE fldLaneID = @laneID;
		
END
GO

CREATE PROCEDURE getParticipants
    AS 
	BEGIN
		SELECT * FROM TblParticipants;
		
END
GO

CREATE PROCEDURE getLanes
    AS 
	BEGIN
		SELECT * FROM TblLanes;
		
END
GO

CREATE PROCEDURE getAllShirts
    AS 
	BEGIN
		SELECT * FROM TblShirts;
		
END
GO

---Update Procedures
CREATE PROCEDURE updateScorePoints(@id int,@hitScore VARCHAR(255), @score int)
    AS 
	BEGIN
		UPDATE TblScore SET fldHitScore = @hitScore, fldScore = @score WHERE fldScoreID= @id;

END
GO

CREATE PROCEDURE updateShirt(@color VARCHAR(32), @amount INT, @used BIT)
    AS 
	BEGIN
		UPDATE TblShirts SET fldAmount = @amount, fldUsedColor = @used WHERE fldColor= @color;
END
GO

