package technical;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import application.Controller;
import domain.Lane;
import domain.Participant;
import domain.Score;
import domain.User;

public class DBHandler {

	private Statement statement;
	private Connection connection;
	private static DBHandler instance;
	public static Properties pathProperties = null;
	
	
	public static Properties getProperties() {

		Properties p = new Properties();
		try {
			File test = new File("technicalProperties.properties");
			
			System.out.println(test.getAbsolutePath());
			File f = new File("C:\\Users\\Kristian\\Documents\\Eclipse\\BikeTilting\\BikeTilting\\technicalProperties.properties"); 
			if (f.isDirectory())
				System.out.println("Check if you file is directory: " + f.isDirectory() + " with " + f.listFiles().length + " files");
			else
				System.out.println("Check if you file exists: " + f.exists());
			System.out.println("Directory path: " + f.getAbsolutePath());
			p.load(new FileInputStream(f));
		} catch (IOException e) {
			System.out.println("IO exception to service property file");
		}
		return p;

	}

	private DBHandler (){
		try
		{
			Properties p = getProperties();
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			/*String connectionUrl = "jdbc:sqlserver://"+p.getProperty("host")+":"+p.getProperty("port")+
					";user="+p.getProperty("user")
					+";password="+p.getProperty("password")
					+";databaseName="+p.getProperty("databaseName")+"";*/

			String connectionUrl = "jdbc:sqlserver://localhost:1433;user=Kristian;password=1234;databaseName=BikeTiltingDB";
			
			connection = DriverManager.getConnection(connectionUrl);
			statement = connection.createStatement();

		}
		catch (SQLException e) {
			System.err.println("SQL Server Exception: Client info error.\n");
			e.printStackTrace();
		}
		catch (ClassNotFoundException cnfe) {
			System.err.println("Class not found Exeption: JDBC drivers are not installed correctly");
		}
	}


	public static DBHandler getInstance(){
		if (instance == null) 
			instance = new DBHandler();
		return instance;
	}

	public Statement getStatement() {
		return statement;
	}

	public void close() {
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Connection getConnection() {
		return connection;
	}

	/*
	 	Put various info inside the DB with stored Procedures
	 */

	public User createUser(String cpr, String fName, String lName, String email, String password, String phoneNumber, int accessLevel) {

		try {		
			CallableStatement cs = connection.prepareCall("{call createParticipant(?,?,?,?,?,?,?)}");

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

	public Participant createParticipant(String fName, String lName, String ageRange, String email, Score score, Color shirtColor, Integer shirtNumber) {

		try {	

			CallableStatement cs = connection.prepareCall("{call createParticipant(?,?,?,?,?,?,?,?,?)}");


			cs.setString(1,fName);
			cs.setString(2,lName);
			cs.setString(3,ageRange);
			cs.setString(4,email);
			cs.setInt(5,score.getScoreID());
			cs.setString(6,getColorString(shirtColor));
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

			CallableStatement cs = connection.prepareCall("{call addParticipant(?,?,?,?,?,?,?,?,?)}");
			cs.setString(1,fName);
			cs.setString(2,lName);
			cs.setString(3,ageRange);
			cs.setString(4,email);

			//Create Score

			CallableStatement csScore = connection.prepareCall("{call  createScore(?,?,?)}");


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


	public String createSQLQuery(String table, Object... data) {
		String sql = "INSERT INTO " + table + "(";

		for (int i = 0; i < data.length; i += 2) 
			sql += data[i] + ",";

		sql = sql.substring(0, sql.length()-1) + ") VALUES (";

		for (int i = 1; i < data.length; i += 2) 
			sql += ((isNumber(data[i])) ? data[i] : "'"+data[i]+"'") + ",";

		return sql.substring(0, sql.length()-1) + ")";
	}

	private boolean isNumber(Object o) {
		return o instanceof Byte || o instanceof Short || o instanceof Integer || o instanceof Long || o instanceof Double || o instanceof Float;
	}

	/*
	 	Get various Info from the DB with stored Procedures
	 */

	public List<User> getAllUsers() {

		List<User> users = new ArrayList<>();

		try {

			CallableStatement csUser = connection.prepareCall("{call getUser}");
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

			CallableStatement cs = connection.prepareCall("{call getParticipant}");

			ResultSet rs = cs.executeQuery();

			while(rs.next()){

				int scoreID = rs.getInt("fldScoreID");
				CallableStatement csScore = connection.prepareCall("{call getScoreByID(?)");
				cs.setInt(1, scoreID);
				ResultSet rsScore = csScore.executeQuery();
				Score score = new Score(scoreID, rsScore.getInt("fldScore"), rsScore.getString("fldHitScore"));

				Participant p = new Participant(rs.getInt("fldParticipantID"),
						rs.getString("fldFName"), 
						rs.getString("fldLName"),
						rs.getString("fldAgeRange"),
						rs.getString("fldEmail"), 
						score,
						getColor(rs.getString("fldShirtColour")),
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

			CallableStatement cs = connection.prepareCall("{call getParticipant(?)}");
			cs.setInt(1, id);
			ResultSet rs = cs.executeQuery();

			while(rs.next()){

				int scoreID = rs.getInt("fldScoreID");
				CallableStatement csScore = connection.prepareCall("{call getParticipant(?)}");
				cs.setInt(1, scoreID);

				ResultSet rsScore = csScore.executeQuery();
				rsScore.next();

				Score score = new Score(scoreID, rsScore.getInt("fldScore"), rsScore.getString("fldHitScore"));

				return new Participant(rs.getInt("fldParticipentID"),
						rs.getString("fldFName"), 
						rs.getString("fldLName"),
						rs.getString("fldAgeRange"),
						rs.getString("fldEmail"), 
						score,
						getColor(rs.getString("fldShirtColour")),
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

			CallableStatement csLane = connection.prepareCall("{call getLane}");

			ResultSet rs = csLane.executeQuery();

			while(rs.next()){

				Lane l = new Lane( rs.getInt("fldLaneNr"));
				CallableStatement csPart = connection.prepareCall("{call getParticipantsByLaneID(?)}");
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

	/*
	    converting colour to be stored in the DB 
	 */

	private Color getColor(String colorRepresentation) {
		String[] sColor = colorRepresentation.split(",");
		return new Color(Integer.parseInt(sColor[0])/255, Integer.parseInt(sColor[1])/255, Integer.parseInt(sColor[2])/255);
	}

	private String getColorString(Color c) {
		int r = (int) (c.getRed()*255);
		int g = (int) (c.getGreen()*255);
		int b = (int) (c.getBlue()*255);
		return r+","+g+","+b;
	}
	
	
	/*
	    Score Editor
	 */
	
	public boolean updateScore(Score score){

		try{

			CallableStatement csUpdateScore = connection.prepareCall("{call updateScorePoints(?,?,?)}");
			
			
			csUpdateScore.setInt(0,score.getScoreID());
			csUpdateScore.setString(1,score.getHitScore());
			csUpdateScore.setInt(2,score.getScore());
			
			csUpdateScore.execute();
			
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

}