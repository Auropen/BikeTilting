package application;

import java.util.List;

import domain.BikeTilting;
import domain.Lane;
import domain.Participant;
import domain.Score;
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

	//Utilise methods
	@Override
	public List<Participant> searchParticipant(String fName, String lName, String ageRange, String shirtColor, Integer shirtNumber) {
		return bikeTilting.searchParticipants(fName, lName, ageRange, shirtColor, shirtNumber);
	}

	@Override
	public void addParticipant(Participant p) {
		bikeTilting.getParticipants().add(p);
	}

	@Override
	public void addHit(Participant p) {
		bikeTilting.addScoreHit(p);
		dbHandler.updateScore(p.getScore());
	}

	@Override
	public void addMiss(Participant p) {
		bikeTilting.addScoreMiss(p);
		dbHandler.updateScore(p.getScore());
	}

	@Override
	public void undoScore(Participant p) {
		bikeTilting.undoScore(p);
		dbHandler.updateScore(p.getScore());
	}
	
	@Override
	public void storeDBToMemory() {
		bikeTilting.storeDBToMemory();
	}

	public void laneGeneration(int laneAmount) {
		bikeTilting.generateLanes(laneAmount);
	}
	
	//Create methods
	@Override
	public Participant createParticipantToDB(String fName, String lName, String ageRange, String email, Score score, String shirtColor, Integer shirtNumber) {
		return dbHandler.createParticipant(fName, lName, ageRange, email, score, shirtColor, shirtNumber);
	}

	@Override
	public Participant createParticipantToDB(String fName, String lName, String ageRange, String email) {
		return dbHandler.createParticipant(fName, lName, ageRange, email);
	}
	
	@Override
	public Lane createLaneToDB(int laneNr, String ageGroup) {
		return dbHandler.createLane(laneNr, ageGroup);
	}

	public boolean createColorToDB(String color, int amount , int used) {
		return dbHandler.createColor(color, amount, used);
	}
	
	//Update methods
	@Override
	public boolean updateColor(String color, int amount, int used) {
		return dbHandler.updateColor(color, amount, used);
	}

	@Override
	public boolean updateParticipant(Participant p, Integer laneID) {
		return dbHandler.updateParticipant(p, laneID);
	}
	
	//Get methods
	@Override
	public List<Participant> getParticipantsFromDB() {
		return dbHandler.getAllParticipants();
	}

	@Override
	public List<Participant> getParticipants() {
		return bikeTilting.getParticipants();
	}

	@Override
	public List<Lane> getLanesFromDB() {
		return dbHandler.getAllLanes();
	}

	@Override
	public List<Lane> getLanes() {
		return bikeTilting.getLanes();
	}

	@Override
	public List<String> getColorsFromDB() {
		return dbHandler.getAllColors();
	}
	
	@Override
	public Lane getLaneFromID(int laneID) {
		return bikeTilting.getLaneFromID(laneID);
	}

	@Override
	public Participant getParticipant(int participantID) {
		for (Participant p : bikeTilting.getParticipants())
			if (p.getId() == participantID) return p;
		return null;
	}

	@Override
	public Participant getParticipantFromDB(int participantID) {
		return dbHandler.getParticipant(participantID);
	}

	@Override
	public Score getScoreFromDB(int scoreID) {
		return dbHandler.getScoreByID(scoreID);
	}
	
	@Override
	public Lane getParticipantLane(Participant p) {
		return bikeTilting.getParticipantLane(p);
	}
}
