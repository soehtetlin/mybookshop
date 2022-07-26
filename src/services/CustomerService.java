package services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import database_config.DBconnector;
import entities.Customer;
import repositories.CustomerRepo;
import shared.mapper.CustomerMapper;
import shared.mapper.GeneratePrimaryKey;

public class CustomerService implements CustomerRepo {

	private final DBconnector dbConfig;
	private CustomerMapper customerMapper;
	private CustomerRepo customerRepo;
	private GeneratePrimaryKey generateClass;

	public CustomerService() {
		this.dbConfig = new DBconnector();
		this.customerMapper = new CustomerMapper();
		this.customerMapper.setCustomerRepo(this);
		this.generateClass = new GeneratePrimaryKey();
	}

	@Override
	public void saveCustomer(Customer customer) {
		try {

			customer.setId(generateClass.generateID("id", "Customer", "CS"));

			PreparedStatement ps = this.dbConfig.getConnection().prepareStatement(
					"INSERT INTO customer (id, name, contact_no, email, address, register_date, expired_date, last_date_use, active) "
							+ "VALUES (?, ?, ?, ?,?, ?, ?, ?,?)");

			ps.setString(1, customer.getId());
			ps.setString(2, customer.getName());
			ps.setString(3, customer.getContact_no());
			ps.setString(4, customer.getEmail());
			ps.setString(5, customer.getAddress());
			ps.setString(6, String.valueOf(customer.getRegister_date()));
			ps.setString(7, String.valueOf(customer.getExpired_date()));

			ps.setString(8, String.valueOf(customer.getLast_date_use()));
			ps.setInt(9, customer.getActive());

			ps.close();

			JOptionPane.showMessageDialog(null, "Record Saved Successfully.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int CountCustomer() {
		int customercount = 0;

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "select count(*) from customer;";

			ResultSet rs = st.executeQuery(query);
			rs.next();
			customercount = rs.getInt("count(*)");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return customercount;
	}

	@Override
	public void updateCustomer(String cusId, Customer customer) {

		try {

			PreparedStatement ps = this.dbConfig.getConnection()
					.prepareStatement("UPDATE customer SET name = ?, contact_no=?, email=?, address=?,"
							+ "register_date=?, expired_date=?, last_date_use=?, active=? WHERE id=?");

			ps.setString(1, customer.getName());
			ps.setString(2, customer.getContact_no());
			ps.setString(3, customer.getEmail());
			ps.setString(4, customer.getAddress());
			ps.setString(5, String.valueOf(customer.getRegister_date()));
			ps.setString(6, String.valueOf(customer.getExpired_date()));
			ps.setString(7, String.valueOf(customer.getLast_date_use()));
			ps.setInt(8, customer.getActive());
			ps.setString(9, cusId);

			ps.close();
		} catch (Exception e) {
			if (e instanceof SQLIntegrityConstraintViolationException)
				JOptionPane.showMessageDialog(null, e.getMessage());
			else
				e.printStackTrace();
		}

	}

	public void updateCustomerLatestDateUse(String cusId) {

		try {

			PreparedStatement ps = this.dbConfig.getConnection()
					.prepareStatement("UPDATE customer SET last_date_use=?,expired_date=? WHERE id=?");

			ps.setString(1, LocalDateTime.now().toString());
			ps.setString(2, LocalDateTime.now().plusYears(2).toString());

			ps.setString(3, cusId);

			ps.close();
		} catch (Exception e) {
			if (e instanceof SQLIntegrityConstraintViolationException)
				JOptionPane.showMessageDialog(null, e.getMessage());
			else
				e.printStackTrace();
		}

	}

	public void updateAutoNoActive(String customerId) {
		try {
			PreparedStatement ps = this.dbConfig.getConnection()
					.prepareStatement("UPDATE customer SET active=? WHERE id=?");

			ps.setInt(1, 0);

			ps.setString(2, customerId);

			ps.close();

		} catch (Exception e) {
			if (e instanceof SQLIntegrityConstraintViolationException)
				JOptionPane.showMessageDialog(null, e.getMessage());
			else
				e.printStackTrace();
		}
	}

	@Override
	public void detleteCustomer(Customer customer) {

		try {
			LocalDateTime expiredDate = customer.getExpired_date();
			LocalDateTime toDeleteDate = expiredDate.plusYears(2);

			System.out.println("Expired Date : " + expiredDate + "delete date : " + toDeleteDate);

			if (expiredDate.compareTo(toDeleteDate) < 0) {

			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "You cannot delete this publisher");
		}

	}

	@Override
	public List<Customer> findAllCustomers() {

		List<Customer> customerList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM customer ORDER BY customer.id DESC;";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Customer customer = new Customer();

				customerList.add(this.customerMapper.mapToCustomer(customer, rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customerList;
	}

	public List<Customer> findbyactive() {

		List<Customer> customerList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM customer where active = 1 ORDER BY customer.id DESC;";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Customer customer = new Customer();

				customerList.add(this.customerMapper.mapToCustomer(customer, rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customerList;
	}

	public Customer findAllCustomersByCustomerName(String cid) {

		Customer customer = new Customer();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT active FROM customer where id='" + cid + "';";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {

				customer.setActive(rs.getInt("active"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customer;
	}

	public Customer findByName(String name) {
		Customer customer = new Customer();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM customer WHERE name = '" + name + "';";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				customer.setId(rs.getString("id"));
				customer.setName(rs.getString("name"));
				customer.setContact_no(rs.getString("contact_no"));
				customer.setAddress(rs.getString("address"));
				customer.setEmail(rs.getString("email"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return customer;
	}

	@Override
	public Customer findCustomerById(String id) {

		Customer customer = new Customer();
		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			System.out.println("Customer id " + id);
			String query = "SELECT * FROM customer WHERE id = '" + id + "';";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {

				customer.setId(rs.getString("id"));
				customer.setName(rs.getString("name"));
				customer.setContact_no(rs.getString("contact_no"));
				customer.setEmail(rs.getString("email"));
				customer.setAddress(rs.getString("address"));
				customer.setRegister_date(LocalDateTime.parse(rs.getString("register_date"),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

				customer.setExpired_date(LocalDateTime.parse(rs.getString("expired_date"),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				customer.setLast_date_use(LocalDateTime.parse(rs.getString("last_date_use"),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				customer.setActive(rs.getInt("active"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customer;
	}

	@Override
	public List<Customer> findCustomersByActive(int active) {

		System.out.println("Active " + active);
		List<Customer> customerList = new ArrayList<>();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM customer WHERE active='" + active + "';";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Customer customer = new Customer();

				customerList.add(this.customerMapper.mapToCustomer(customer, rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customerList;
	}

	public int sumCustomerAmountForOneMonth(String id) {
		int total = 0;

		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "select sum(sale_price*quantity) from sale_detail inner join sale on sale_detail.vouncher_id=sale.id "
					+ "where sale.customer_id='" + id + "' AND month(sale.sale_date)=month(localtime()) ;";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				total = rs.getInt(1);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return total;
	}

}
