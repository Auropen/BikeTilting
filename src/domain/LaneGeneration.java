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
		Integer shirtNumber = 1;
		String latestColorUsed = "";
		
		for (String ageGroup : ageGroups) {
			List<Participant> partPList = new ArrayList<Participant>();
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
			int pIStart = 0;
			//Fills the lanes
			for (int i = 0; i < laneAmounts; i++) {
				Lane l = iCtr.createLaneToDB(i+1, ageGroup);
				int laneSize = size + ((sizeRemain > 0) ? 1 : 0);
				
				String color = getFirstUsableColor(laneSize);
				
				System.out.println("Using color: " + color + " with size usage: " + (size + ((sizeRemain > 0) ? 1 : 0)));	
				colors.add(color);
				String[] colorData = color.split(",");
				if (!latestColorUsed.equals(colorData[0]))
					shirtNumber = 1;
				latestColorUsed = colorData[0];
				
				for (int pI = pIStart; pI < pIStart + laneSize;pI++) {
					Participant p = partPList.get(pI);
					p.setShirtColor(colorData[0]);
					p.setShirtNumber(shirtNumber++);
					l.addParticipant(p);
					iCtr.updateParticipant(p, l.getLaneID());
				}

				iCtr.updateColor(colorData[0], Integer.parseInt(colorData[1]), Integer.parseInt(colorData[2]) + size);
				lanes.add(l);
				
				pIStart += laneSize;
				//Decrement remainder
				sizeRemain--;
			}
			
			//Makes sure the used color isn't reused, by other age groups.
			for (String c : colors) {
				String[] colorData = c.split(",");
				iCtr.updateColor(colorData[0], Integer.parseInt(colorData[1]), Integer.parseInt(colorData[1]));
			}
		}
		
		return lanes;
	}
	
	public static String getFirstUsableColor(int needed) {
		List<String> colors = iCtr.getColorsFromDB();
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
		List<String> colors = iCtr.getColorsFromDB();
		for (String s : colors) {
			String[] colorData = s.split(",");
			int used = Integer.parseInt(colorData[2]);
			if (used == 0)
				return s;
		}
		return null;
	}
}
