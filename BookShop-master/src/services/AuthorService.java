package services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLIntegrityConstraintViolationException;

import entities.Author;
import repositories.AuthorRepo;
import database_config.DBconnector;
import shared.exception.AppException;

import javax.swing.*;

public class AuthorService implements AuthorRepo {

	private final DBconnector dbConfig = new DBconnector();

	@Override
	public void saveAuthor(Author author) {
		// TODO Auto-generated method stub
		try {
			author.setId(generateID("id", "Author", "AU"));
			PreparedStatement ps = this.dbConfig.getConnection()
					.prepareStatement("INSERT INTO Author (id,name)  VALUES (?,?);");
			ps.setString(1, author.getId());
			ps.setString(2, author.getName());
			ps.executeUpdate();
			ps.close();
			JOptionPane.showMessageDialog(null, "Record Saved Successfully.");

		} catch (SQLException e) {
			if (e instanceof SQLIntegrityConstraintViolationException) {
				JOptionPane.showMessageDialog(null, "Please Enter Name");
			}

		}

	}

	@Override
	public void deleteAuthor(String id) {
		// TODO Auto-generated method stub
		try {
			String query = "DELETE FROM Author WHERE id = ?";
			PreparedStatement ps = this.dbConfig.getConnection().prepareStatement(query);
			ps.setString(1, id);
			ps.executeUpdate();
			ps.close();
			JOptionPane.showMessageDialog(null, "Delete Successfully!");
		} catch (Exception e) {
			if (e instanceof AppException)
				JOptionPane.showMessageDialog(null, e.getMessage());
			else
				e.printStackTrace();
		}
	}

	@Override
	public void updateAuthor(String id, Author category) {
		// TODO Auto-generated method stub
		try {

			PreparedStatement ps = this.dbConfig.getConnection()
					.prepareStatement("UPDATE Author SET name = ? WHERE id = ?");

			ps.setString(1, category.getName());
			ps.setString(2, id);
			ps.executeUpdate();
			ps.close();
			JOptionPane.showMessageDialog(null, "Record Update Successfully.");

		} catch (Exception e) {
			if (e instanceof SQLIntegrityConstraintViolationException) {
				JOptionPane.showMessageDialog(null, "Already Exists");
			}
		}
	}

	@Override
	public List<Author> findAllAuthors() {
		// TODO Auto-generated method stub
		List<Author> authorlist = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM Author";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Author author = new Author();
				author.setId(rs.getString("id"));
				author.setName(rs.getString("name"));
				authorlist.add(author);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return authorlist;

	}

	@Override
	public Author findById(String id) {
		// TODO Auto-generated method stub
		Author author = new Author();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM Author WHERE id = '" + id + "';";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				author.setId(rs.getString("id"));
				author.setName(rs.getString("name"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return author;
	}

	public String generateID(String field, String table, String prefix) {
		ResultSet rs = null;
		int current;

		try {
			Statement smt = dbConfig.getConnection().createStatement();
			rs = smt.executeQuery("SELECT " + field + " FROM " + table + " ORDER BY id");
			ArrayList<String> result = new ArrayList<String>();
			while (rs.next()) {
				result.add(rs.getString(field));

			}

			if (result.size() > 0) {
				current = Integer.parseInt(result.get(result.size() - 1).substring(2, 10)) + 1;
				if (current > 0 && current <= 9) {
					return prefix + "0000000" + current;
				} else if (current > 9 && current <= 99) {
					return prefix + "000000" + current;
				} else if (current > 99 && current <= 999) {
					return prefix + "00000" + current;
				} else if (current > 999 && current <= 9999) {
					return prefix + "0000" + current;
				} else if (current > 9999 && current <= 99999) {
					return prefix + "000" + current;
				} else if (current > 99999 && current <= 999999) {
					return prefix + "00" + current;
				} else if (current > 999999 && current <= 9999999) {
					return prefix + "0" + current;
				} else if (current > 9999999 && current <= 9999999) {
					return prefix + current;
				}
			}
		} catch (SQLException e) {
		}
		return prefix + "00000001";
	}
	
	public String generateID2(String field, String table, String prefix) {
		ResultSet rs = null;
		int current;

		try {
			Statement smt = dbConfig.getConnection().createStatement();
			rs = smt.executeQuery("SELECT " + field + " FROM " + table + " ORDER BY id");
			ArrayList<String> result = new ArrayList<String>();
			while (rs.next()) {
				result.add(rs.getString(field));

			}

			System.out.println("Result size "+ result.size());
			
			if (result.size() > 0) {
				
				System.out.println("Current ; "+ Integer.parseInt(result.get(result.size() - 1).toString().substring(3, 10)) + 1);

				current = Integer.parseInt(result.get(result.size() - 1).toString().substring(3, 10)) + 1;
				if (current > 0 && current <= 9) {
					return prefix + "000000" + current;
				} else if (current > 9 && current <= 99) {
					return prefix + "00000" + current;
				} else if (current > 99 && current <= 999) {
					return prefix + "0000" + current;
				} else if (current > 999 && current <= 9999) {
					return prefix + "000" + current;
				} else if (current > 9999 && current <= 99999) {
					return prefix + "00" + current;
				} else if (current > 99999 && current <= 999999) {
					return prefix + "0" + current;
				} else if (current > 999999 && current <= 999999) {
					return prefix + current;
				}
			}
		} catch (SQLException e) {
		}
		return prefix + "0000001";
	}

}
