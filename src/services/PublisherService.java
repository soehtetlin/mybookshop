package services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import database_config.DBconnector;
import entities.Employee;
import entities.Publisher;
import entities.Purchase;
import services.AuthorService;
import shared.exception.AppException;
import shared.mapper.GeneratePrimaryKey;

public class PublisherService {
	
	private GeneratePrimaryKey genPrimaryKey = new GeneratePrimaryKey();

	private final DBconnector dbConfig = new DBconnector();

	public void savePublisher(Publisher publisher) {
		try {
			AuthorService authService = new AuthorService();

			publisher.setId(genPrimaryKey.generateID2("id", "Publisher", "Pub"));
			PreparedStatement ps = this.dbConfig.getConnection()
					.prepareStatement("INSERT INTO publisher (id,name, contact_no,address, email) VALUES (?,?,?,?,?);");

			ps.setString(1, publisher.getId());
			ps.setString(2, publisher.getName());
			ps.setString(3, publisher.getContact_no());
			ps.setString(4, publisher.getAddress());
			ps.setString(5, publisher.getEmail());

			ps.executeUpdate();
			ps.close();
			JOptionPane.showMessageDialog(null, "Record Saved Successfully.");

		} catch (SQLException e) {
			// if (e instanceof MySQLIntegrityConstraintViolationException)
			if (e instanceof SQLIntegrityConstraintViolationException) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}

	}

	public void updatePublisher(String id, Publisher publisher) {
		try {

			PreparedStatement ps = this.dbConfig.getConnection().prepareStatement(
					"UPDATE publisher SET name = ? , contact_no = ? , address = ? , email = ?  WHERE id = ?");

			ps.setString(1, publisher.getName());
			ps.setString(2, publisher.getContact_no());
			ps.setString(3, publisher.getAddress());
			ps.setString(4, publisher.getEmail());
			ps.setString(5, id);
			ps.execute();

			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Publisher> findAllPublishers() {

		List<Publisher> publisherList = new ArrayList<>();
		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM publisher ORDER BY publisher.id DESC;";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Publisher p = new Publisher();
				p.setId(rs.getString("id"));
				p.setName(rs.getString("name"));
				p.setContact_no(rs.getString("contact_no"));
				p.setAddress(rs.getString("address"));
				p.setEmail(rs.getString("email"));
				publisherList.add(p);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return publisherList;
	}

	public Publisher findById(String id) {
		Publisher publisher = new Publisher();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM publisher WHERE id = '" + id + "';";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				publisher.setId(rs.getString("id"));
				publisher.setName(rs.getString("name"));
				publisher.setContact_no(rs.getString("contact_no"));
				publisher.setAddress(rs.getString("address"));
				publisher.setEmail(rs.getString("email"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return publisher;
	}

	public void deletePublisher(String pubId) {
		try {
			List<Purchase> purchaseByPublisherId = findPurchaseListByPublisherId(pubId);

			if (purchaseByPublisherId.size() > 0) {
				throw new AppException("This publisher cannot be deleted");
			}

			PreparedStatement ps = this.dbConfig.getConnection().prepareStatement("DELETE FROM publisher WHERE id = ?");
			ps.setString(1, pubId);

			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "You cannot delete this publisher");
		}
	}

	public List<Purchase> findPurchaseListByPublisherId(String publisherId) {

		List<Purchase> purchaseList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM purchase\n" + "INNER JOIN employee\n"
					+ "on employee.id = purchase.employee_id\n" + "INNER JOIN publisher\n"
					+ "ON publisher.id = purchase.publisher_id\n" + "WHERE publisher_id='" + publisherId + "';";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Purchase purchase = new Purchase();

				purchase.setId(rs.getString("id"));
				purchase.setPurchaseDate(LocalDateTime.parse(rs.getString("purchaseDate"),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
				purchase.setDescription(rs.getString("description"));

				Employee employee = new Employee();
				employee.setId(rs.getString("id"));
				purchase.setEmployee(employee);

				Publisher publisher = new Publisher();
				purchase.setPublisher(publisher);

				purchaseList.add(purchase);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return purchaseList;
	}

	public Publisher findByName(String name) {
		Publisher publisher = new Publisher();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM publisher WHERE name = '" + name + "';";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				publisher.setId(rs.getString("id"));
				publisher.setName(rs.getString("name"));
				publisher.setContact_no(rs.getString("contact_no"));
				publisher.setAddress(rs.getString("address"));
				publisher.setEmail(rs.getString("email"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return publisher;
	}

}
