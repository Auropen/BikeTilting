package domain;

import java.awt.Color;

public class Participant {
	
	private String fName;
	private String lName;
	private String email;
	private String ageRange;
	private Score score;
	private Color color;

	public Participant(String fName, String lName, String ageRange, String email, Score score, Color color) {
		this.setFName(fName);
		this.setLName(lName);
		this.setEmail(email);
		this.setAgeRange(ageRange);
		this.setScore(score);
		this.setColor(color);
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

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
