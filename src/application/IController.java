package application;

import java.util.List;

import domain.Lane;
import domain.Participant;
import domain.Score;
import domain.User;

public interface IController {
	public List<User> getUsersFromDB();
	public List<Participant> getParticipantsFromDB();
	public List<Participant> getParticipants();
	public List<Lane> getLanesFromDB();
	public List<Lane> getLanes();
	public void storeDBToMemory();
	public Lane getLaneFromLaneNr(int laneNr);
	public Participant createParticipantToDB(String fName, String lName, String ageRange, String email, Score score, String shirtColor, Integer shirtNumber);
	public Participant createParticipantToDB(String fName, String lName, String ageRange, String email);
	public Participant getParticipantFromDB(int participantID);
	public Participant getParticipant(int participantID);
	public List<Participant> searchParticipant(String fName, String lName, String ageRange, String shirtColor, Integer shirtNumber);
	public void addParticipant(Participant p);
	public void addHit(Participant p);
	public void addMiss(Participant p);
}
