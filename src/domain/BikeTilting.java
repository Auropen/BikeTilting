/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kornel
 */
public class BikeTilting {

    /**
     * @param aInstance the instance to set
     */
    public static void setInstance(BikeTilting aInstance) {
        instance = aInstance;
    }

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
        if (instance == null) {
            instance = new BikeTilting();
        }
        return instance;
    }

    public boolean createUser(String cpr, String fName, String lName, String email, String password, String phoneNumber, int accessLevel) {
        getUsers().add(new User(cpr, fName, lName, email, password, phoneNumber, accessLevel));
        return true;
    }

    public boolean createParticipant(String fName, String lName, String ageRange, String email, String scoreID, String colour, int laneNr) {
        getParticipants().add(new Participant(fName, lName, ageRange, email, scoreID, colour, laneNr));
        return true;
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
}
