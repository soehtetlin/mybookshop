//package shared.mapper;
//
//import entities.Publisher;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class PublisherMapper {
//
//	public Publisher mapToSupplier(Publisher publisher, ResultSet rs) {
//
//		try {
//			publisher.setId(rs.getString("sup_id"));
//			publisher.setName(rs.getString("sup_name"));
//			publisher.setAddress(rs.getString("sup_address"));
//			publisher.setEmail(rs.getString("sup_email"));
//			publisher.setPhone(rs.getString("sup_phone"));
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return publisher;
//	}
//}
