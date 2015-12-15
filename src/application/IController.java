package application;

import java.util.List;

import domain.Lane;
import domain.Participant;
import domain.Score;

public interface IController {
	//Utilise methods
	public void storeDBToMemory();
	public List<Participant> searchParticipant(String fName, String lName, String ageRange, String shirtColor, Integer shirtNumber);
	public void addParticipant(Participant p);
	public void addHit(Participant p);
	public void addMiss(Participant p);
	public void undoScore(Participant p);
	public void laneGeneration(int laneAmount);
	
	//Create methods
	public Participant createParticipantToDB(String fName, String lName, String ageRange, String email, Score score, String shirtColor, Integer shirtNumber);
	public Participant createParticipantToDB(String fName, String lName, String ageRange, String email);
	public Lane createLaneToDB(int laneNr, String ageGroup);
	public boolean createColorToDB(String color, int amount , int usedColor);
	
	//Update methods
	public boolean updateColor(String color, int amount, int used);
	public boolean updateParticipant(Participant p, Integer laneID);
	
	//Get methods
	public List<Participant> getParticipantsFromDB();
	public List<Participant> getParticipants();
	public List<Lane> getLanesFromDB();
	public List<Lane> getLanes();
	public List<String> getColorsFromDB();
	public Lane getLaneFromID(int laneID);
	public Participant getParticipantFromDB(int participantID);
	public Participant getParticipant(int participantID);
	public Score getScoreFromDB(int scoreID);
	public Lane getParticipantLane(Participant p);
}
