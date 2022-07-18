package entities;

import java.util.ArrayList;
import java.util.List;

public class Publisher {

	private String id;

	private String name;

	private String contact_no;

	private String address;

	private String email;

	public Publisher() {
	}

	public Publisher(String id, String name, String phone, String address, String email) {
		super();
		this.id = id;
		this.name = name;
		this.contact_no = phone;
		this.address = address;
		this.email = email;
	}

	public String getContact_no() {
		return contact_no;
	}

	public void setContact_no(String contact_no) {
		this.contact_no = contact_no;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

}
