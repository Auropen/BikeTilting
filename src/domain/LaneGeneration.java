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
				String color = getFirstUnusedColor();
				colors.add(color);
				Integer shirtNumber = 0;
				for (int pI = size * i; pI < size * (i+1);pI++) {
					Participant p = partPList.get(pI);
					p.setShirtColor(color);
					p.setShirtNumber(shirtNumber++);
					l.addParticipant(p);
				}
				
				lanes.add(l);
			}
			
			//Filling in the remains after division
			for (int lI = 0; lI < sizeRemain; lI++) {
				int pI = size * laneAmounts + lI;
				Participant p = partPList.get(pI);
				p.setShirtColor(colors.get(lI));
				p.setShirtNumber(lanes.get(lI).getParticipants().size() + 1);
				lanes.get(lI).addParticipant(partPList.get(pI));
			}
		}
		
		return lanes;
	}
	
	public static String getFirstUnusedColor() {
		List<String> colors = iCtr.getShirtsFromDB();
		for (String s : colors) {
			String[] colorData = s.split(",");
			if (colorData[2].equals("false")) {
				iCtr.updateShirt(colorData[0],Integer.parseInt(colorData[1]),Boolean.getBoolean(colorData[2]));
				return colorData[0] + "," + colorData[1];
			}
		}
		return null;
	}
}
