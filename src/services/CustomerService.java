package services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
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
    
	public CustomerService(){
		this.dbConfig = new DBconnector();
		this.customerMapper=new CustomerMapper();
		this.customerMapper.setCustomerRepo(this);
		this.generateClass= new GeneratePrimaryKey();
	}
	
	@Override
	public void saveCustomer(Customer customer) {
		try {
			
			customer.setId(generateClass.generateID("id", "Customer", "CS"));

			PreparedStatement ps=this.dbConfig.getConnection().prepareStatement(
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
			ps.setBoolean(9, customer.getActive());
			System.out.println("db "+String.valueOf(customer.getRegister_date()));
			ps.executeUpdate();

			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateCustomer(String cusId, Customer customer) {
		// TODO Auto-generated method stub

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
			ps.setBoolean(8, customer.getActive());
			ps.setString(9, cusId);
			
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            if (e instanceof SQLIntegrityConstraintViolationException)
                JOptionPane.showMessageDialog(null, e.getMessage());
            else e.printStackTrace();
        }
    
	}

	@Override
	public void detleteCustomer(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Customer> findAllCustomers() {

		List<Customer> customerList= new ArrayList<>();
		
		try(Statement st = this.dbConfig.getConnection().createStatement()){
			
			String query= "SELECT * FROM customer";
			
			ResultSet rs= st.executeQuery(query);
			
			while(rs.next()) {
				Customer customer= new Customer();
				System.out.println("Date: "+ customer.getRegister_date());
				customerList.add(this.customerMapper.mapToCustomer(customer, rs));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return customerList;
    }
	

	@Override
	public Customer findCustomerById(String Id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Customer> findCustomersByActive(Boolean active) {

		System.out.println("Active "+ active);
		List<Customer> customerList= new ArrayList<>();
		
		try(Statement st = this.dbConfig.getConnection().createStatement()){
			
			String query= "SELECT * FROM customer WHERE active='" + active + "';";
			
			ResultSet rs= st.executeQuery(query);
			
			while(rs.next()) {
				Customer customer= new Customer();
				System.out.println("Date: "+ customer.getRegister_date());
				customerList.add(this.customerMapper.mapToCustomer(customer, rs));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return customerList;
    }

}
