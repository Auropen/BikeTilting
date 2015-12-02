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
        users.add(new User(cpr, fName, lName, email, password, phoneNumber, accessLevel));
        return true;
    }

    public boolean createParticipant(String fName, String lName, String ageRange, String email, String scoreID, String colour, int laneNr) {
        participants.add(new Participant(fName, lName, ageRange, email, scoreID, colour, laneNr));
        return true;
    }

    public boolean createLane(int laneNr) {
        lanes.add(new Lane(laneNr));
        return true;
    }
    
    public boolean addScoreHit(Participant p){
        p.getScore().addHit();
    }
    
    public boolean addScoreMiss(Participant p){
        p.getScore().addMiss();
    }
}
