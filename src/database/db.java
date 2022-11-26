package database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class db {
	private static String url = "jdbc:mysql://localhost:3306/mydb";
	private static String user = "root";
	private static String password = "admin";
	private static Connection conn = null;

	public static Connection getConnection() {
		if (conn == null) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (Exception ex) {
				System.out.println(ex);
			}

			try {
				conn = DriverManager.getConnection(url, user, password);

			} catch (SQLException ex) {
				// handle any errors
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}
		}
		return conn;
	}
}