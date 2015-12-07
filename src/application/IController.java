package application;

import java.awt.Color;
import java.util.List;

import domain.Lane;
import domain.Participant;
import domain.Score;
import domain.User;

public interface IController {
	public List<User> getUsersFromDB();
	public List<Participant> getParticipantsFromDB();
	public List<Lane> getLanesFromDB();
	public Participant createParticipantToDB(String fName, String lName, String ageRange, String email, Score score, Color shirtColor, Integer shirtNumber);
	public Participant createParticipantToDB(String fName, String lName, String ageRange, String email);
	public Participant getParticipantFromDB(int participantID);
	public Participant getParticipant(int participantID);
	public void addParticipant(Participant p);
	public void addHit(Participant p);
	public void addMiss(Participant p);
}
