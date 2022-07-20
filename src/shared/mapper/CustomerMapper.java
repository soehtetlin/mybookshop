package shared.mapper;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.text.DateFormatter;

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
			
			customer.setExpired_date(LocalDate.parse(rs.getString("register_date")));
			customer.setRegister_date(LocalDate.parse(rs.getString("register_date")));
			customer.setLast_date_use(LocalDate.parse(rs.getString("register_date")));
			
//			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSX");
			
//			LocalDateTime result = LocalDateTime.parse(rs.getString("register_date"), format);
//			customer.setRegister_date(result);
			
//			LocalDateTime result1 = LocalDateTime.parse(rs.getString("register_date"), format);
//			customer.setExpired_date(result1);
			
//			LocalDateTime result2 = LocalDateTime.parse(rs.getString("register_date"), format);
//			customer.setLast_date_use(result2);
			customer.setActive(rs.getBoolean("active"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return customer;
	}

}
