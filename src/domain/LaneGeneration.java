package domain;

import java.util.ArrayList;
import java.util.List;

import application.Controller;
import application.IController;

public class LaneGeneration {
	private static IController iCtr = Controller.getInstance();
	
	public static List<Lane> generate(int laneAmounts) {
		String[] ageGroups = {"0-6","7-9","10-14"};
		List<Lane> lanes = new ArrayList<Lane>();
		List<Participant> pList = iCtr.getParticipants();
		List<Participant> partPList = new ArrayList<Participant>();
		
		for (String ageGroup : ageGroups) {
			List<String> colors = new ArrayList<String>();
			//Get all participant in the specific age group
			for (Participant p : pList) {
				if (p.getAgeRange().equals(ageGroup)) {
					partPList.add(p);
				}
			}
			
			//Gets the divided value and remains
			int size = partPList.size();
			int sizeRemain = size % laneAmounts;
			size /= laneAmounts;
			
			//Fills the lanes
			for (int i = 0; i < laneAmounts; i++) {
				Lane l = iCtr.createLaneToDB(i+1,ageGroup);
				String color = getFirstUsableColor(size + ((sizeRemain > 0) ? 1 : 0));
				System.out.println("Using color: " + color + " with size usage: " + (size + ((sizeRemain > 0) ? 1 : 0)));	
				colors.add(color);
				String[] colorData = color.split(",");
				Integer shirtNumber = 0;
				for (int pI = size * i; pI < size * (i+1) + ((sizeRemain > 0) ? 1 : 0);pI++) {
					Participant p = partPList.get(pI);
					p.setShirtColor(colorData[0]);
					p.setShirtNumber(shirtNumber++);
					l.addParticipant(p);
				}

				iCtr.updateShirt(colorData[0], Integer.parseInt(colorData[1]), Integer.parseInt(colorData[2]) + size);
				lanes.add(l);
				
				//Decrement remainder
				sizeRemain--;
			}
			
			//Makes sure the used color isn't reused, by other age groups.
			//@@@@@ NEED BETTER SOLUTION!!! @@@@
			for (String c : colors) {
				String[] colorData = c.split(",");
				iCtr.updateShirt(colorData[0], Integer.parseInt(colorData[1]), Integer.parseInt(colorData[1]));
			}
		}
		
		return lanes;
	}
	
	public static String getFirstUsableColor(int needed) {
		List<String> colors = iCtr.getShirtsFromDB();
		for (String s : colors) {
			String[] colorData = s.split(",");
			int amount = Integer.parseInt(colorData[1]);
			int used = Integer.parseInt(colorData[2]);
			System.out.println(colors + " -> " + amount + "," + used);
			if (amount - used > needed)
				return s;
		}
		return null;
	}
	
	public static String getFirstUnusedColor() {
		List<String> colors = iCtr.getShirtsFromDB();
		for (String s : colors) {
			String[] colorData = s.split(",");
			int used = Integer.parseInt(colorData[2]);
			if (used == 0)
				return s;
		}
		return null;
	}
}
