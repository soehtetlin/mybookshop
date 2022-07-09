package services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import database_config.DBconnector;
import entities.Publisher;
import services.AuthorService;
import shared.exception.AppException;

public class PublisherService {

	private final DBconnector dbConfig = new DBconnector();
	

	public void savePublisher(Publisher publisher) {
        try {
        	AuthorService authService=new AuthorService();
        	
        	publisher.setId(authService.generateID2("id", "Publisher", "Pub"));
            PreparedStatement ps = this.dbConfig.getConnection()
                    .prepareStatement("INSERT INTO publisher (id,name, contact_no,address, email) VALUES (?,?,?,?,?);");

            ps.setString(1, publisher.getId());
            ps.setString(2, publisher.getName());
            ps.setString(3, publisher.getContact_no());
            ps.setString(4, publisher.getAddress());
            ps.setString(5, publisher.getEmail());
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null,"Record Saved Successfully.");

        } catch (SQLException e) {
           // if (e instanceof MySQLIntegrityConstraintViolationException)
        	 if (e instanceof SQLIntegrityConstraintViolationException){
                JOptionPane.showMessageDialog(null,e.getMessage());
            }
        }

    }


	public void updatePublisher(String id, Publisher publisher) {
		try {

			PreparedStatement ps = this.dbConfig.getConnection()
					.prepareStatement("UPDATE publisher SET name = ? , contact_no = ? , address = ? , email = ?  WHERE id = ?");

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

			String query = "SELECT * FROM publisher";

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

			String query = "SELECT * FROM publisher WHERE id = " + id + ";";

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

	public void delete(String id) {
		try {

//			List<Product> productsByCategoryId = this.productRepo.findProductsByBrandId(id);
//
//			if (productsByCategoryId.size() > 0) {
//				throw new AppException("This brand cannot be deleted");
//			}

			String query = "DELETE FROM brand WHERE brand_id = ?";

			PreparedStatement ps = this.dbConfig.getConnection().prepareStatement(query);
			ps.setString(1, id);

			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			if (e instanceof AppException)
				JOptionPane.showMessageDialog(null, e.getMessage());
			else
				e.printStackTrace();
		}
	}

}
