package entities;

public class Employee extends PersonInfo {

	private String username;

	private String password;

	// private UserRole role;

	// private boolean active;

	public Employee() {
		super();
	}

	public Employee(String username, String password) {
		super();
		this.username = username;
		this.password = password;
		// this.role = role;
		// this.active = active;
	}

//	public boolean isActive() {
//		return active;
//	}

//	public void setActive(boolean active) {
//		this.active = active;
//	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

//	public UserRole getRole() {
//		return role;
//	}
//
//	public void setRole(UserRole role) {
//		this.role = role;
//	}

}
