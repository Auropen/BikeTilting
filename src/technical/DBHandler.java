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

import com.sun.istack.internal.Nullable;

import application.Controller;
import application.IController;
import domain.Lane;
import domain.Participant;
import domain.Score;

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
			cs.setNull(6,java.sql.Types.VARCHAR);
			cs.setNull(7,java.sql.Types.INTEGER);
			cs.setNull(8,java.sql.Types.INTEGER);
			cs.registerOutParameter(9, java.sql.Types.INTEGER);

			cs.execute();

			int participantID = cs.getInt(9);

			return new Participant(participantID, fName, lName, ageRange, email, score, null, null);

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean createColor(String color, int amount , int used) {
		try {
			CallableStatement cs = getConnection().prepareCall("{call createColor(?,?,?)}");
			cs.setString(1,color);
			cs.setInt(2, amount);
			cs.setInt(3, used);

			cs.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Lane createLane(int laneNr, String ageGroup) {
		try {
			CallableStatement cs = getConnection().prepareCall("{call createLane(?,?,?)}");
			cs.setInt(1, laneNr);
			cs.setString(2, ageGroup);
			cs.registerOutParameter(3, java.sql.Types.INTEGER);

			cs.execute();

			return new Lane(cs.getInt(3), laneNr, ageGroup);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	//Get various Info from the DB with stored Procedures

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

				Lane l = new Lane(rs.getInt("fldLaneID"), rs.getInt("fldLaneNr"), rs.getString("fldAgeGroup"));
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
	
	public List<String> getAllColors(){

		List<String> shirts = new ArrayList<>();
		
		try {
			CallableStatement cs = getConnection().prepareCall("{call getAllColors}");
			ResultSet rs = cs.executeQuery();

			while(rs.next()){
				String s = rs.getString("fldColor")+","+rs.getString("fldAmount")+","+rs.getInt("fldUsedAmount");
				shirts.add(s);
			}
			return shirts;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Score getScoreByID(int id){
		
		try {
			CallableStatement cs = getConnection().prepareCall("{call getScoreByID(?)}");
			cs.setInt(1, id);
			ResultSet rs = cs.executeQuery();

			rs.next();
			return new Score(id, rs.getInt("fldScore"), rs.getString("fldHitScore"));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	    Update score
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
	
	public boolean updateColor(String color, int amount, int used){
		try{
			CallableStatement cs = getConnection().prepareCall("{call updateColor(?,?,?)}");
			cs.setString(1,color);
			cs.setInt(2,amount);
			cs.setInt(3,used);

			cs.execute();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateParticipant(Participant p, @Nullable Integer laneID){
		try{
			IController iCtr = Controller.getInstance();
			CallableStatement cs = getConnection().prepareCall("{call updateParticipant(?,?,?,?,?,?,?,?)}");
			cs.setInt(1,p.getId());
			cs.setString(2,p.getFName());
			cs.setString(3,p.getLName());
			cs.setString(4,p.getAgeRange());
			cs.setString(5,p.getEmail());
			cs.setString(6,p.getShirtColor());
			cs.setInt(7,p.getShirtNumber());
			cs.setInt(8,laneID);

			cs.execute();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}