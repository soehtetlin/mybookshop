package shared.mapper;

import java.sql.ResultSet;

import entities.Employee;

public class EmployeeMapper {

	public Employee mapToEmployee(Employee employee, ResultSet rs) {
		try {
			employee.setId(rs.getString("id"));
			employee.setName(rs.getString("name"));
			employee.setPhone(rs.getString("contact_no"));
			employee.setEmail(rs.getString("email"));
			employee.setAddress(rs.getString("address"));
			employee.setUsername(rs.getString("username"));
			employee.setPassword(rs.getString("password"));
			employee.setage(rs.getInt("age"));
			employee.setgender(rs.getString("gender"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return employee;
	}
}
