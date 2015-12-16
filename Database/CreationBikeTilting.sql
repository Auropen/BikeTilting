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
							 fldShirtNumber INT,  
							 fldLaneID INT FOREIGN KEY REFERENCES TblLanes (fldLaneID))

CREATE TABLE TblColors		(fldColor VARCHAR(32) UNIQUE NOT NULL,
							 fldAmount INT NOT NULL,
							 fldUsedAmount INT NOT NULL)

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

CREATE PROCEDURE createScore(@hitScore VARCHAR(255), @score INT, @new_id INT OUTPUT)
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

CREATE PROCEDURE createColor(@color VARCHAR(32), @amount INT, @used INT)
    AS 
	BEGIN
		INSERT INTO TblColors
		(
			fldColor,
			fldAmount,
			fldUsedAmount
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

CREATE PROCEDURE getAllColors
    AS 
	BEGIN
		SELECT * FROM TblColors;
		
END
GO

---Update Procedures
CREATE PROCEDURE updateScorePoints(@id int,@hitScore VARCHAR(255), @score int)
    AS 
	BEGIN
		UPDATE TblScore SET fldHitScore = @hitScore, fldScore = @score WHERE fldScoreID= @id;

END
GO

CREATE PROCEDURE updateColor(@color VARCHAR(32), @amount INT, @used INT)
    AS 
	BEGIN
		UPDATE TblColors SET fldAmount = @amount, fldUsedAmount = @used WHERE UPPER(fldColor) = UPPER(@color);
END
GO

CREATE PROCEDURE updateParticipant(@id INT, @fName VARCHAR(32), @lName VARCHAR(32), @ageRange VARCHAR(6), @email VARCHAR(255), @shirtColour VARCHAR(12), @shirtNumber INT, @laneID INT)
    AS 
	BEGIN
		UPDATE TblParticipants 
		SET 
			fldFName = @fName,
			fldLName = @lName,
			fldAgeRange = @ageRange,
			fldEmail = @email,
			fldShirtColour = @shirtColour,
			fldShirtNumber = @shirtNumber,
			fldLaneID = @laneID
		WHERE fldParticipantID = @id;
END
GO

