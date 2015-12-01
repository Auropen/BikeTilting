package technical;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

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
}

