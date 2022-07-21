package entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Customer {

	private String id;
	private String name;
	private String contact_no;
	private String email;
	private String address;
	private LocalDateTime register_date;
	private LocalDateTime expired_date;
	private LocalDateTime last_date_use;
	private int active;
	
	
	public Customer() {
		super();
	}
	
	
	public Customer(String id, String name, String contact_no, String email, String address,
			LocalDateTime register_date, LocalDateTime expired_date, LocalDateTime last_date_use, int active) {
		super();
		this.id = id;
		this.name = name;
		this.contact_no = contact_no;
		this.email = email;
		this.address = address;
		this.register_date = register_date;
		this.expired_date = expired_date;
		this.last_date_use = last_date_use;
		this.active = active;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContact_no() {
		return contact_no;
	}
	public void setContact_no(String contact_no) {
		this.contact_no = contact_no;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
//	public LocalDate getRegister_date() {
//		return register_date;
//	}
//	public void setRegister_date(LocalDate register_date) {
//		this.register_date = register_date;
//	}
//	public LocalDate getExpired_date() {
//		return expired_date;
//	}
//	public void setExpired_date(LocalDate expired_date) {
//		this.expired_date = expired_date;
//	}
//	public LocalDate getLast_date_use() {
//		return last_date_use;
//	}
//	public void setLast_date_use(LocalDate last_date_use) {
//		this.last_date_use = last_date_use;
//	}
	public int getActive() {
		return active;
	}
	public LocalDateTime getRegister_date() {
		return register_date;
	}


	public void setRegister_date(LocalDateTime register_date) {
		this.register_date = register_date;
	}


	public LocalDateTime getExpired_date() {
		return expired_date;
	}


	public void setExpired_date(LocalDateTime expired_date) {
		this.expired_date = expired_date;
	}


	public LocalDateTime getLast_date_use() {
		return last_date_use;
	}


	public void setLast_date_use(LocalDateTime last_date_use) {
		this.last_date_use = last_date_use;
	}


	public void setActive(int active) {
		this.active = active;
	}
	
	
}
