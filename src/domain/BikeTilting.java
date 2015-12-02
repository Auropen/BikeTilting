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

    public BikeTilting() {
        this.users = new ArrayList<User>();
        this.participants = new ArrayList<Participant>();
    }

    private boolean createUser(String cpr, String fName, String lName, String email, String password, String phoneNumber, int accessLevel) {
        users.add(new User(cpr, fName, lName, email, password, phoneNumber, accessLevel));
        return true;
    }

    private boolean createParticipant(String fName, String lName, String ageRange, String email, String scoreID, String colour, int laneNr) {
        participants.add(new Participant(fName, lName, ageRange, email, scoreID, colour, laneNr));
        return true;
    }
}
