package entities;

public abstract class PersonInfo {

	private String id;

	private String name;

	private String contact_no;

	private String address;

	private String email;

	private String gender;

	private int age;

	public PersonInfo() {
	}

	public PersonInfo(String id, String name, String phone, String email, String gender, int age, String address) {
		super();
		this.id = id;
		this.name = name;
		this.contact_no = phone;
		this.email = email;
		this.age = age;
		this.gender = gender;
		this.address = address;
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

	public String getPhone() {
		return contact_no;
	}

	public void setPhone(String phone) {
		this.contact_no = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setgender(String gender) {
		this.gender = gender;
	}

	public String getgender() {
		return gender;
	}

	public int getage() {
		return age;
	}

	public void setage(int age) {
		this.age = age;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

}
