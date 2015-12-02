package application;

import java.util.List;

import domain.BikeTilting;
import domain.Lane;
import domain.Participant;
import domain.User;
import technical.DBHandler;

public class Controller implements IController {
	private static IController instance;
	private DBHandler dbHandler;
	private BikeTilting bikeTilting;

	private Controller() {
		this.dbHandler = DBHandler.getInstance();
		this.bikeTilting = BikeTilting.getInstance();
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
		return dbHandler.getAllLanes();
	}

	@Override
	public void addParticipantToDB(Participant p) {
		
	}

	@Override
	public Participant getParticipant(int participantID) {
		for (Participant p : bikeTilting.getParticipants())
			if (p.getId() == participantID) return p;
		return null;
	}
}
