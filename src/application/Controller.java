package application;

import java.awt.Color;
import java.util.List;

import domain.BikeTilting;
import domain.Lane;
import domain.Participant;
import domain.Score;
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
	public Participant getParticipant(int participantID) {
		for (Participant p : bikeTilting.getParticipants())
			if (p.getId() == participantID) return p;
		return null;
	}

	@Override
	public Participant addParticipantToDB(String fName, String lName, String ageRange, String email, Score score, Color shirtColor, Integer shirtNumber) {
		return dbHandler.addParticipant(fName, lName, ageRange, email, score, shirtColor, shirtNumber);
	}

	@Override
	public Participant getParticipantFromDB(int participantID) {
		return dbHandler.getParticipant(participantID);
	}

	@Override
	public void addParticipant(Participant p) {
		bikeTilting.getParticipants().add(p);
	}

	@Override
	public void addHit(Participant p) {
		bikeTilting.addScoreHit(p);
	}

	@Override
	public void addMiss(Participant p) {
		bikeTilting.addScoreMiss(p);
	}
}
