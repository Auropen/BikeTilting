/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.ArrayList;
import java.util.List;

import application.Controller;


public class BikeTilting {
	private List<User> users;
	private List<Participant> participants;
	private List<Lane> lanes;
	private static BikeTilting instance;

	private BikeTilting() {
		this.users = new ArrayList<User>();
		this.participants = new ArrayList<Participant>();
		this.lanes = new ArrayList<Lane>();
	}

	public static BikeTilting getInstance() {
		if (instance == null)
			instance = new BikeTilting();
		return instance;
	}

	public void createUser(String cpr, String fName, String lName, String email, String password, String phoneNumber, int accessLevel) {
		getUsers().add(new User(cpr, fName, lName, email, password, phoneNumber, accessLevel));
	}

	public void createParticipant(int id, String fName, String lName, String ageRange, String email, Score score, String shirtColor, Integer shirtNumber) {
		getParticipants().add(new Participant(id, fName, lName, ageRange, email, score, shirtColor, shirtNumber));
	}

	public boolean createLane(int laneNr) {
		getLanes().add(new Lane(laneNr));
		return true;
	}

	public void addScoreHit(Participant p){
		p.getScore().addHit();
	}

	public void addScoreMiss(Participant p){
		p.getScore().addMiss();
	}
	
	public Lane getLaneFromLaneNr(int laneNr) {
		for (Lane lane : lanes) {
			if (lane.getLaneNr() == laneNr)
				return lane;
		}
		return null;
	}

	public List<Participant> searchParticipants(String fName, String lName, String ageRange, String shirtColor, Integer shirtNumber) {
		List<Participant> allParticipant = Controller.getInstance().getParticipantsFromDB();
		List<Participant> searchResult = new ArrayList<Participant>();
		
		searchResult.addAll(allParticipant);
		
		for (Participant p : allParticipant) {
			if (!fName.isEmpty() && !p.getFName().equalsIgnoreCase(fName)) {
				searchResult.remove(p);
				continue;
			}
			if (!lName.isEmpty() && !p.getLName().equalsIgnoreCase(lName)) {
				searchResult.remove(p);
				continue;
			}
			if (!ageRange.isEmpty() && !p.getAgeRange().equalsIgnoreCase(ageRange)) {
				searchResult.remove(p);
				continue;
			}
			if (!shirtColor.isEmpty() && !p.getShirtColor().equalsIgnoreCase(shirtColor)) {
				searchResult.remove(p);
				continue;
			}
			if (shirtNumber != null && p.getShirtNumber() != shirtNumber) {
				searchResult.remove(p);
				continue;
			}
		}
		
		return searchResult;
	}
	
	/**
	 * @return the users
	 */
	
	public List<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * @return the participants
	 */
	public List<Participant> getParticipants() {
		return participants;
	}

	/**
	 * @param participants the participants to set
	 */
	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

	/**
	 * @return the lanes
	 */
	
	public List<Lane> getLanes() {
		return lanes;
	}

	/**
	 * @param lanes the lanes to set
	 */
	
	public void setLanes(List<Lane> lanes) {
		this.lanes = lanes;
	}
	
	public boolean storeDBToMemory(){
		participants = Controller.getInstance().getParticipantsFromDB();
		lanes = Controller.getInstance().getLanesFromDB();
		if(participants == null && lanes == null)
			return false;
		return true;
	}
}
