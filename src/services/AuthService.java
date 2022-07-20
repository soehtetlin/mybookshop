package services;

import database_config.*;
import entities.Employee;
import repositories.AuthRepo;
import shared.exception.AppException;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthService implements AuthRepo {

	private final DBconnector dbConfig;

	public AuthService() {
		dbConfig = new DBconnector();
	}

	public String login(String username, String password) {
		String id = "";
		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = String.format("SELECT * FROM employee \n" + "WHERE username=\"%s\" AND password=\"%s\";",
					username, password);

			ResultSet rs = st.executeQuery(query);

			if (rs.next()) {
				id = rs.getString("id");
			} else {
				JOptionPane.showMessageDialog(null, "Invalid Credential");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
}
