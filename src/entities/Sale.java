package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sale {

	private String id;

	private Employee employee;

	private Customer customer;

	private LocalDateTime saleDate;

	private List<Sale> sales = new ArrayList<>();

	public Sale() {
	}

	public Sale(String id, Employee employee, Customer customer, LocalDateTime saleDate, List<Sale> sales) {
		super();
		this.id = id;
		this.saleDate = saleDate;
		this.employee = employee;
		this.customer = customer;
		this.sales = sales;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public LocalDateTime getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(LocalDateTime saleDate) {
		this.saleDate = saleDate;
	}

	public List<Sale> getSales() {
		return sales;
	}

	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}

}
