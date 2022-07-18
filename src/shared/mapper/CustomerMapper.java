package shared.mapper;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import entities.Customer;
import repositories.CustomerRepo;
import repositories.PurchaseRepo;

public class CustomerMapper {
	private CustomerRepo customerRepo;
	
	public void setCustomerRepo(CustomerRepo customerRepo) {
		this.customerRepo = customerRepo;
	}
	
	public Customer mapToCustomer(Customer customer, ResultSet rs) {
		try {
			customer.setId(rs.getString("id"));
			customer.setName(rs.getString("name"));
			customer.setContact_no(rs.getString("contact_no"));
			customer.setEmail(rs.getString("email"));
			customer.setAddress(rs.getString("address"));
			customer.setRegister_date(LocalDateTime.parse(rs.getString("register_date"), 
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			customer.setRegister_date(LocalDateTime.parse(rs.getString("register_date"), 
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			customer.setRegister_date(LocalDateTime.parse(rs.getString("register_date"), 
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			customer.setActive(rs.getBoolean("active"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return customer;
	}

}
