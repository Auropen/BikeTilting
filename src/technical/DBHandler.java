package technical;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import application.Controller;
import domain.Lane;
import domain.Participant;
import domain.Score;
import domain.User;

public class DBHandler {
	private static DBHandler instance;
	private String dbName;
	private static Properties p = null;


	public static void getProperties(File propertyFile) {
		p = new Properties();
		try {
			p.load(new FileInputStream(propertyFile));
		} catch (IOException e) {
			System.out.println("IO exception to service property file");
		}
	}

	private DBHandler (){
	}


	public static DBHandler getInstance(){
		if (instance == null) 
			instance = new DBHandler();
		return instance;
	}

	private Connection getConnection() {
		try {
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			dbName = p.getProperty("databaseName");
			
			String connectionUrl = "jdbc:sqlserver://"+p.getProperty("host")+":"+p.getProperty("port")
			+";user="+p.getProperty("user")
			+";password="+p.getProperty("password")
			+";databaseName="+dbName;

			//String connectionUrl = "jdbc:sqlserver://localhost:1433;user=Kristian;password=1234;databaseName=BikeTiltingDB";

			return DriverManager.getConnection(connectionUrl);
		}
		catch (SQLException e) {
			System.err.println("SQL Server Exception: Client info error.\n");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Put various info inside the DB with stored Procedures
	 * @param cpr
	 * @param fName
	 * @param lName
	 * @param email
	 * @param password
	 * @param phoneNumber
	 * @param accessLevel
	 * @return
	 */
	
	public User createUser(String cpr, String fName, String lName, String email, String password, String phoneNumber, int accessLevel) {

		try {		
			CallableStatement cs = getConnection().prepareCall("{call createParticipant(?,?,?,?,?,?,?)}");

			cs.setString(1,cpr);
			cs.setString(2,fName);
			cs.setString(3,lName);
			cs.setString(4,email);
			cs.setString(5,password);
			cs.setString(6,phoneNumber);
			cs.setInt(7,accessLevel);

			cs.execute();

			return new User(cpr, fName, lName, email, password, phoneNumber, accessLevel);

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Participant createParticipant(String fName, String lName, String ageRange, String email, Score score, String shirtColor, Integer shirtNumber) {
		try {
			CallableStatement cs = getConnection().prepareCall("{call createParticipant(?,?,?,?,?,?,?,?,?)}");

			cs.setString(1,fName);
			cs.setString(2,lName);
			cs.setString(3,ageRange);
			cs.setString(4,email);
			cs.setInt(5,score.getScoreID());
			cs.setString(6,shirtColor);
			cs.setInt(7,shirtNumber);
			cs.registerOutParameter(8, java.sql.Types.INTEGER);
			cs.execute();
			int participantID = cs.getInt(8);
			
			return new Participant(participantID, fName, lName, ageRange, email, score, shirtColor, shirtNumber);

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	public Participant createParticipant(String fName, String lName, String ageRange, String email) {
		try {
			CallableStatement cs = getConnection().prepareCall("{call createParticipant(?,?,?,?,?,?,?,?,?)}");
			cs.setString(1,fName);
			cs.setString(2,lName);
			cs.setString(3,ageRange);
			cs.setString(4,email);

			//Create Score
			CallableStatement csScore = getConnection().prepareCall("{call createScore(?,?,?)}");


			csScore.setString(1,"");
			csScore.setInt(2,0);
			csScore.registerOutParameter(3, java.sql.Types.INTEGER);
			csScore.execute();
			int scoreID = csScore.getInt(3);
			Score score = new Score(scoreID);

			cs.setInt(5, scoreID);
			cs.setString(6,null);
			cs.setInt(7,-1);
			cs.setInt(8,1);
			cs.registerOutParameter(9, java.sql.Types.INTEGER);

			cs.execute();

			int participantID = cs.getInt(9);

			return new Participant(participantID, fName, lName, ageRange, email, score, null, null);

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean createShirt(String color, int amount , boolean usedColor ) {
		try {
			CallableStatement cs = getConnection().prepareCall("{call createColor(?,?,?)}");
			cs.setString(1,color);
			cs.setInt(2, amount);
			cs.setBoolean(3, usedColor);

			cs.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 	Get various Info from the DB with stored Procedures
	 */

	public List<User> getAllUsers() {

		List<User> users = new ArrayList<>();

		try {

			CallableStatement csUser = getConnection().prepareCall("{call getUser}");
			ResultSet rsUser = csUser.executeQuery();

			while(rsUser.next()){

				User u = new User(rsUser.getString("fldCPR"), 
						rsUser.getString("fldPassword"), 
						rsUser.getString("fldFName"), 
						rsUser.getString("fldLName"), 
						rsUser.getString("fldEmail"), 
						rsUser.getString("fldPhoneNumber"), 
						rsUser.getInt("fldAccessLevel"));	
				users.add(u);

			}

			return users;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<Participant> getAllParticipants() {
		List<Participant> participants = new ArrayList<>();
		try {
			CallableStatement cs = getConnection().prepareCall("{call getParticipants}");
			ResultSet rs = cs.executeQuery();

			while(rs.next()){

				int scoreID = rs.getInt("fldScoreID");
				CallableStatement csScore = getConnection().prepareCall("{call getScoreByID(?)}");
				csScore.setInt(1, scoreID);
				ResultSet rsScore = csScore.executeQuery();
				rsScore.next();
				Score score = new Score(scoreID, rsScore.getInt("fldScore"), rsScore.getString("fldHitScore"));

				Participant p = new Participant(rs.getInt("fldParticipantID"),
						rs.getString("fldFName"), 
						rs.getString("fldLName"),
						rs.getString("fldAgeRange"),
						rs.getString("fldEmail"), 
						score,
						rs.getString("fldShirtColour"),
						rs.getInt("fldShirtNumber"));
				participants.add(p);
			}

			return participants;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public Participant getParticipant(int id) {
		try {

			CallableStatement cs = getConnection().prepareCall("{call getParticipant(?)}");
			cs.setInt(1, id);
			ResultSet rs = cs.executeQuery();
			
			while(rs.next()){

				int scoreID = rs.getInt("fldScoreID");
				CallableStatement csScore = getConnection().prepareCall("{call getScoreByID(?)}");
				csScore.setInt(1, scoreID);

				ResultSet rsScore = csScore.executeQuery();
				rsScore.next();

				Score score = new Score(scoreID, rsScore.getInt("fldScore"), rsScore.getString("fldHitScore"));

				return new Participant(rs.getInt("fldParticipantID"),
						rs.getString("fldFName"), 
						rs.getString("fldLName"),
						rs.getString("fldAgeRange"),
						rs.getString("fldEmail"), 
						score,
						rs.getString("fldShirtColour"),
						rs.getInt("fldShirtNumber"));	
			}

			return null;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}


	}

	public List<Lane> getAllLanes() {
		List<Lane> lanes = new ArrayList<>();

		try {
			CallableStatement cs = getConnection().prepareCall("{call getLanes}");
			ResultSet rs = cs.executeQuery();

			while(rs.next()){

				Lane l = new Lane( rs.getInt("fldLaneNr"));
				CallableStatement csPart = getConnection().prepareCall("{call getParticipantsByLaneID(?)}");
				csPart.setInt(1, rs.getInt("fldLaneID"));

				ResultSet rsPart = csPart.executeQuery();

				while(rsPart.next())
					l.addParticipant(Controller.getInstance().getParticipant(rsPart.getInt("fldParticipantID")));

				lanes.add(l);	

			}

			return lanes;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public List<String> getAllShirts(){

		List<String> shirts = new ArrayList<>();
		
		try {
			CallableStatement cs = getConnection().prepareCall("{call getAllShirts}");
			ResultSet rs = cs.executeQuery();

			while(rs.next()){

				String s = rs.getString("fldColor")+","+rs.getString("fldAmount")+","+rs.getBoolean("fldUsedColor");
				shirts.add(s);	

			}
			return shirts;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	/*
	    Score Editor
	 */

	public boolean updateScore(Score score){

		try{

			CallableStatement csUpdateScore = getConnection().prepareCall("{call updateScorePoints(?,?,?)}");


			csUpdateScore.setInt(1,score.getScoreID());
			csUpdateScore.setString(2,score.getHitScore());
			csUpdateScore.setInt(3,score.getScore());

			csUpdateScore.execute();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}
}