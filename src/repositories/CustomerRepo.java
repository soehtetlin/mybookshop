package repositories;

import java.util.List;

import entities.Customer;

public interface CustomerRepo {
	
	void saveCustomer(Customer customer);
	
	void updateCustomer(String cusId, Customer customer);
	
	void detleteCustomer(Customer customer);
	
	List<Customer> findAllCustomers();
	
	Customer findCustomerById(String Id);
	
	List<Customer> findCustomersByActive(int active);

}
