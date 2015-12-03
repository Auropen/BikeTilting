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

}

