package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

	private static Connection conn;

	public static Connection getConnection() throws SQLException { // dzieki getConnection nie trzeba bedzie za kazdym razem
																	// wywolywac polaczenia

		if (conn == null) {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/warsztat2?useSSL=false", "root",
					"coderslab");
		}
		return conn;
	}

	
}
