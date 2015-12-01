package technical;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import domain.Participant;
import domain.User;

public class DBHandler {
	
	private Statement statement;
	private Connection connection;
	private static DBHandler instance;
	
    public static Properties getProperties() {

        Properties p = new Properties();
        try {
            p.load(new FileInputStream("service.properties"));
        } catch (IOException e) {
            System.out.println("IO exception to service property file");
        }
        return p;

    }
	@SuppressWarnings("unused")
	private DBHandler (){
		try
		{
            Properties p = getProperties();
            String user = p.getProperty("user");
            String password = p.getProperty("password");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = p.getProperty("connectionUrl","jdbc:sqlserver://localhost:1434;user=sa;password=1234;databaseName=SecondhandCarsDB");
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
    
    
    public static boolean addUser(User user) {

    	String test = "INSERT INTO TblUsers (fldCPR,"            +
    									    "fldPassword,"       +
    			                            "fldFName, "         +
    			                            "fldLName, "         +
    			                            "fldEmail,"          +
    			                            "fldPhoneNumber,"    +
    			                            "fldAccessLevel ) "  +
    			                            
    			      "VALUES('" + user.getCPR() + "',"          +
    			      		 "'" + user.getPassword() + "',"	 +
    			      	     "'" + user.getFName() + "',"        +
    			      	     "'" + user.getLName() + "',"        +
    			      	     "'" + user.getEmail() + "',"        +
    			      	     "'" + user.getPhoneNumber() + "',"  +
    			      	     	   user.getAccessLevel() + ")";

    	try {
    		DBHandler con = DBHandler.getInstance();
    		con.getStatement().execute(test);
    		return true;

    	} catch (SQLException e) {
    		e.printStackTrace();
    		return false;
    	}
    }
    
    
    public static boolean addParticipant(Participant participants) {

    	String test = "INSERT INTO TblParticipants (fldFName, "         +
    									           "fldLName, "         +
    									           "fldAgeRange,"       +
    									           "fldEmail,"          +
    									           "fldScoreID,"        +
    									           "fldColour,"         +
    									           "fldLaneNr ) "       +
    									           
    			      "VALUES('" + participants.getFName() + "',"       +
    			      		 "'" + participants.getLName() + "',"	    +
    			      	     "'" + participants.getAgeRange() + "',"    +
    			      	     "'" + participants.getEmail() + "',"       +
    			      	     "'" + participants.getScoreID() + "',"     +
    			      	     "'" + participants.getColour() + "',"      +
    			      	           participants.getLaneNr() + ")";

    	try {
    		DBHandler con = DBHandler.getInstance();
    		con.getStatement().execute(test);
    		return true;

    	} catch (SQLException e) {
    		e.printStackTrace();
    		return false;
    	}
    }
    
    
}

