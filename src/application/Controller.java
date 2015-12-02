package application;

import java.util.List;

import domain.Lane;
import domain.Participant;
import domain.User;
import technical.DBHandler;

public class Controller implements IController {
	private static IController instance;
	private DBHandler dbHandler;

	private Controller() {
		this.dbHandler = DBHandler.getInstance();
	}

	public static IController getInstance() {
		if (instance == null) instance = new Controller();
		return instance;
	}

	@Override
	public List<User> getUsersFromDB() {
		return dbHandler.getAllUsers();
	}

	@Override
	public List<Participant> getParticipantsFromDB() {
		return dbHandler.getAllParticipants();
	}

	@Override
	public List<Lane> getLanesFromDB() {
		return dbHandler.get
	}

	@Override
	public void addParticipantToDB(String email, String fName, String lName, String ageGroup) {
		// TODO Auto-generated method stub
		
	}
}
