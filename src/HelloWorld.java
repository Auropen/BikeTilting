
import java.io.File;

import application.Controller;
import application.IController;
import domain.BikeTilting;
import domain.Participant;
import technical.DBHandler;


public class HelloWorld {
	public static void main(String[] args) {
		
		IController ictr = Controller.getInstance();
		
		DBHandler.getProperties(new File("technicalProperties.properties"));
		
		BikeTilting bike = BikeTilting.getInstance();
		
		bike.storeDBToMemory();
		
		System.out.println(bike.getParticipants().size());
		
		System.out.println(bike.getLanes().size());
		
		for (Participant participant : ictr.searchParticipant("Cornel", "", "", "", null)) {
			
			System.out.println(participant.getFName() + " > " + participant.getLName());
			
		}
		
	}
}
