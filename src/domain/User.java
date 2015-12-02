package domain;

public class User {
	
	private String cpr;
	private String fName;
	private String lName;
	private String email;
	private String password;
	private String phoneNumber;
	private int accessLevel;

	public User(String cpr, String fName, String lName, String email, String password, String phoneNumber, int accessLevel) {
		this.setCPR(cpr);
		this.setFName(fName);
		this.setLName(lName);
		this.setEmail(email);
		this.setPassword(password);
		this.setPhoneNumber(phoneNumber);
		this.setAccessLevel(accessLevel);
		
	}

	public String getCPR() {
		return cpr;
	}

	public void setCPR(String cpr) {
		this.cpr = cpr;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}
	
	
	
		
}
