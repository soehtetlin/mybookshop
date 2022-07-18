package database_config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnector {

	private final String CONNECTION = "jdbc:mysql://localhost:3306/bookshopdb";
	private final String PASSWORD = "root";
	private final String USERNAME = "root";
	private static Connection con = null;

	public Connection getConnection() throws SQLException {

		if (null == con) {
			con = (Connection) DriverManager.getConnection(this.CONNECTION, this.USERNAME, this.PASSWORD);
		}
		return con;
	}
}
