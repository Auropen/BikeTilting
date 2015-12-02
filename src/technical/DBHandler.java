package technical;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import application.Controller;
import application.IController;
import domain.Lane;
import domain.Participant;
import domain.Score;
import domain.User;

public class DBHandler {

	private Statement statement;
	private Connection connection;
	private static DBHandler instance;
	IController iCtr;

	public static Properties getProperties() {

		Properties p = new Properties();
		try {
			p.load(new FileInputStream("technicalProperties.properties"));
		} catch (IOException e) {
			System.out.println("IO exception to service property file");
		}
		return p;

	}
	@SuppressWarnings("unused")
	private DBHandler (){
		
		iCtr = Controller.getInstance();
		
		try
		{
			Properties p = getProperties();
			String user = p.getProperty("user");
			String password = p.getProperty("password");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			
			String connectionUrl = "jdbc:sqlserver://"+p.getProperty("host")+":"
													  +p.getProperty("port")+";user="
													  +p.getProperty("user")+";password="
													  +p.getProperty(password)+";databaseName="
													  +p.getProperty("databaseName")+"";
			
			connection = DriverManager.getConnection(connectionUrl, user, password);
			statement = connection.createStatement();

		}
		catch (SQLException e) {
			System.err.println("SQL Server Exception: Client info error.\n");
			e.printStackTrace();
		}
		catch (ClassNotFoundException cnfe) {
			System.err.println("Class not found Exeption: JDBC driver are not installed correctly");
		}
	}


	public static DBHandler getInstance (){
		if (instance == null) instance = new DBHandler();
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


	public boolean addUser(User user) {

		String addingUser = createSQLQuery("TblUsers", "fldCPR"        , user.getCPR(), 
													   "fldPassword"   , user.getPassword() ,
													   "fldFName"      , user.getFName() ,
													   "fldLName"      , user.getLName() , 
													   "fldEmail"      , user.getEmail(), 
													   "fldPhoneNumber", user.getPhoneNumber(), 
													   "fldAccessLevel", user.getAccessLevel());
		try {
			DBHandler con = DBHandler.getInstance();
			con.getStatement().execute(addingUser);
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


	public boolean addParticipant(Participant participants) {

		String addingParticipant = createSQLQuery("TblUsers", "fldFName"       , participants.getFName(), 
														   	  "fldLName"       , participants.getLName(),
															  "fldAgeRange"    , participants.getAgeRange(),
															  "fldEmail"       , participants.getEmail(), 
															  "fldScoreID"     , participants.getScore(), 
															  "fldShirtColour" , participants.getShirtColor(),
															  "fldShirtNumber" , participants.getShirtNumber());

		try {
			DBHandler con = DBHandler.getInstance();
			con.getStatement().execute(addingParticipant);
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
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

	public List<User> getAllUsers() {

		String getAllUsers = selectAllSQLQuery("TblUsers");	
		
		List<User> users = new ArrayList<>();

		try {

			DBHandler con = DBHandler.getInstance();
			ResultSet rs = con.getStatement().executeQuery(getAllUsers);
			
			while(rs.next()){
				
				User u = new User(rs.getString("fldCPR"), 
						          rs.getString("fldPassword"), 
						          rs.getString("fldFName"), 
						          rs.getString("fldLName"), 
						          rs.getString("fldEmail"), 
						          rs.getString("fldPhoneNumber"), 
						          rs.getInt("fldAccessLevel"));	
			   users.add(u);
				
			}
		
			return users;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public List<Participant> getAllParticipants() {

		String getAllUsers = selectAllSQLQuery("TblParticipants");	
		
		List<Participant> participants = new ArrayList<>();
		
		
		
		try {

			DBHandler con = DBHandler.getInstance();
			ResultSet rs = con.getStatement().executeQuery(getAllUsers);
			
			while(rs.next()){
				int scoreID = rs.getInt("fldScoreID");
				String scoreSql = "SELECT * FROM TblScore WHERE fldScoreID = " + scoreID + ";";
				ResultSet rsScore = con.getStatement().executeQuery(scoreSql);
				rsScore.next();
				
				Score score = new Score(scoreID, rsScore.getInt("fldScore"), rsScore.getString("fldHitScore"));
				
				Participant p = new Participant(rs.getString("fldFName"), 
									            rs.getString("fldLName"),
									            rs.getString("fldAgeRange"),
									            rs.getString("fldEmail"), 
									            score,
									            Color.getColor(rs.getString("fldShirtColour")),
									            rs.getInt("fldShirtNumber"));	
				
				participants.add(p);
				
			}
			
			return participants;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public List<Lane> getAllLanes() {

		String getAllUsers = selectAllSQLQuery("TblUsers");	
		
		List<Lane> lanes = new ArrayList<>();

		try {

			DBHandler con = DBHandler.getInstance();
			ResultSet rs = con.getStatement().executeQuery(getAllUsers);
			
			while(rs.next()){

				
				Lane l = new Lane( rs.getInt("fldLaneNr"));
				
				int laneID = rs.getInt("fldLaneID");
				String partSql = "SELECT * FROM TblParticipants WHERE fldLaneID = " + laneID + ";";
				ResultSet rsPart = con.getStatement().executeQuery(partSql);
				
				
				while(rsPart.next()){
					
					l.addParticipant(iCtr.getParticipant(rsPart.getInt("fldParticipantID")));
					
				}
				
				
				lanes.add(l);
				
			}
			
			
			
			return lanes;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public String selectAllSQLQuery(String table) {
		return "SELECT * FROM " + table + ";";
	}

}

