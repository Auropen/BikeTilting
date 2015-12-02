package application;

import java.util.List;

import domain.Lane;
import domain.Participant;
import domain.User;

public interface IController {
	public List<User> getUsersFromDB();
	public List<Participant> getParticipantsFromDB();
	public List<Lane> getLanesFromDB();
	public void addParticipantToDB(String email, String fName, String lName, String ageGroup);
}
