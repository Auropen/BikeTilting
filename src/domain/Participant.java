package domain;

public class Participant {
	
	private String colour;
	private String fName;
	private String lName;
	private String email;
	private String ageRange;
	private String scoreID;
	private int laneNr;

	public Participant(String fName, String lName, String ageRange, String email, String scoreID, String colour, int laneNr) {
		
		this.setFName(fName);
		this.setLName(lName);
		this.setEmail(email);
		this.setAgeRange(ageRange);
		this.setScoreID(scoreID);
		this.setColour(colour);
		this.setLaneNr(laneNr);
		
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getFName() {
		return fName;
	}

	public void setFName(String fName) {
		this.fName = fName;
	}

	public String getLName() {
		return lName;
	}

	public void setLName(String lName) {
		this.lName = lName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAgeRange() {
		return ageRange;
	}

	public void setAgeRange(String ageRange) {
		this.ageRange = ageRange;
	}

	public String getScoreID() {
		return scoreID;
	}

	public void setScoreID(String scoreID) {
		this.scoreID = scoreID;
	}

	public int getLaneNr() {
		return laneNr;
	}

	public void setLaneNr(int laneNr) {
		this.laneNr = laneNr;
	}
	
	
	
}
