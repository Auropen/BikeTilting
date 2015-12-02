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

    public BikeTilting() {
        this.users = new ArrayList<User>();
    }
    
    private boolean createUser(String cpr, String fName, String lName, String email, String password, String phoneNumber, int accessLevel){
         users.add(new User(cpr, fName, lName, email, password, phoneNumber, accessLevel));
         return true;
    }
}
