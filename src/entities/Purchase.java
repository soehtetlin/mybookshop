package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Purchase {

	private String id;

	private Employee employee;

	private Publisher publisher;

	private LocalDateTime purchaseDate;

	private String description;

	private List<Purchase> purchases = new ArrayList<>();

	public Purchase() {
	}

	public Purchase(String id, Employee employee, Publisher publisher, LocalDateTime purchaseDate, String description,
			List<Purchase> purchases) {
		super();
		this.id = id;
		this.purchaseDate = purchaseDate;
		this.employee = employee;
		this.publisher = publisher;
		this.description = description;
		this.purchases = purchases;
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

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public LocalDateTime getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(LocalDateTime purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}

}
