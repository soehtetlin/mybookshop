package shared.mapper;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import entities.Customer;
import repositories.CustomerRepo;

public class CustomerMapper {
	private CustomerRepo customerRepo;

	public void setCustomerRepo(CustomerRepo customerRepo) {
		this.customerRepo = customerRepo;
	}

	public Customer mapToCustomer(Customer customer, ResultSet rs) throws Exception {

		try {
			customer.setId(rs.getString("id"));
			customer.setName(rs.getString("name"));
			customer.setContact_no(rs.getString("contact_no"));
			customer.setEmail(rs.getString("email"));
			customer.setAddress(rs.getString("address"));
			customer.setActive(rs.getInt("active"));

			customer.setRegister_date(LocalDateTime.parse(rs.getString("register_date"),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

			customer.setExpired_date(LocalDateTime.parse(rs.getString("expired_date"),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

			customer.setLast_date_use(LocalDateTime.parse(rs.getString("last_date_use"),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return customer;
	}

}
