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
	
	//Create methods
	public Participant createParticipantToDB(String fName, String lName, String ageRange, String email, Score score, String shirtColor, Integer shirtNumber);
	public Participant createParticipantToDB(String fName, String lName, String ageRange, String email);
	public Lane createLaneToDB(int laneNr, String ageGroup);
	
	//Update methods
	public boolean updateShirt(String color, int amount, boolean used);
	
	//Get methods
	public List<Participant> getParticipantsFromDB();
	public List<Participant> getParticipants();
	public List<Lane> getLanesFromDB();
	public List<Lane> getLanes();
	public List<String> getShirtsFromDB();
	public Lane getLaneFromLaneNr(int laneNr);
	public Participant getParticipantFromDB(int participantID);
	public Participant getParticipant(int participantID);
	public Score getScoreFromDB(int scoreID);
}
