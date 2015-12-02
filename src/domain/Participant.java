package domain;

import java.awt.Color;

public class Participant {
	
	private String fName;
	private String lName;
	private String email;
	private String ageRange;
	private Score score;
	private Color shirtColor;
	private int shirtNumber;

	public Participant(String fName, String lName, String ageRange, String email, Score score, Color shirtColor, int shirtNumber) {
		this.setFName(fName);
		this.setLName(lName);
		this.setEmail(email);
		this.setAgeRange(ageRange);
		this.setScore(score);
		this.setShirtColor(shirtColor);
		this.setShirtNumber(shirtNumber);
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

	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}

	public Color getShirtColor() {
		return shirtColor;
	}

	public void setShirtColor(Color shirtColor) {
		this.shirtColor = shirtColor;
	}

	public int getShirtNumber() {
		return shirtNumber;
	}

	public void setShirtNumber(int shirtNumber) {
		this.shirtNumber = shirtNumber;
	}
}
