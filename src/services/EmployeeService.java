package services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

//import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import database_config.DBconnector;
import entities.Employee;
import entities.UserRole;
import repositories.EmployeeRepo;
import shared.mapper.EmployeeMapper;

public class EmployeeService implements EmployeeRepo {

	private final EmployeeMapper employeeMapper;
	private final DBconnector dbConfig;

	public EmployeeService() {
		this.employeeMapper = new EmployeeMapper();
		this.dbConfig = new DBconnector();
	}

	public void createEmployee(Employee employee) {
		try {

			PreparedStatement ps = this.dbConfig.getConnection().prepareStatement(
					"INSERT INTO employee (name, contact_no, email, address, emp_level) VALUES (?, ?, ?, ?, ?)");

			ps.setString(1, employee.getName());
			ps.setString(2, employee.getPhone());
			ps.setString(3, employee.getEmail());
			ps.setString(4, employee.getAddress());
			ps.setString(5, UserRole.USER.toString());
			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			// if (e instanceof MySQLIntegrityConstraintViolationException)
			if (e instanceof SQLIntegrityConstraintViolationException) {
				JOptionPane.showMessageDialog(null, "Already Exists");
			}
		}
	}

	public void updateEmployee(String id, Employee employee) {
		try {
			PreparedStatement ps = this.dbConfig.getConnection().prepareStatement(
					"UPDATE employee SET name=?, contact_no=?, email=?, address=?, username=?, password=? WHERE id=?");

			ps.setString(1, employee.getName());
			ps.setString(2, employee.getPhone());
			ps.setString(3, employee.getEmail());
			ps.setString(4, employee.getAddress());
			ps.setString(5, employee.getUsername());
			ps.setString(6, employee.getPassword());
			ps.setString(7, id);

			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			// if (e instanceof MySQLIntegrityConstraintViolationException)
			if (e instanceof SQLIntegrityConstraintViolationException)
				JOptionPane.showMessageDialog(null, "Already Exists");
			else
				e.printStackTrace();
		}
	}

	public void blockEmployee(String id) {
		try {
//			PreparedStatement ps = this.dbConfig.getConnection()
//					.prepareStatement("UPDATE employee SET active=? WHERE emp_id=?");
//
//			ps.setBoolean(1, false);
//			ps.setString(2, id);
//
//			ps.executeUpdate();
//			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Employee> findAllEmployees() {

		List<Employee> employeeList = new ArrayList<>();
		try (Statement st = this.dbConfig.getConnection().createStatement()) {

			String query = "SELECT * FROM employee";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Employee employee = new Employee();
				employeeList.add(this.employeeMapper.mapToEmployee(employee, rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return employeeList;

	}

	public Employee findEmployeeById(String id) {
		Employee employee = new Employee();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {
			
			String query = "SELECT id,name,username,password,age,gender,contact_no,address,email FROM employee WHERE id = '" + id + "';";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				this.employeeMapper.mapToEmployee(employee, rs);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return employee;
	}
	
	public Employee findEmployeeByName(String name) {
		Employee employee = new Employee();

		try (Statement st = this.dbConfig.getConnection().createStatement()) {
			
			String query = "SELECT id,name,username,password,age,gender,contact_no,address,email FROM employee WHERE name = '" + name + "';";

			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				this.employeeMapper.mapToEmployee(employee, rs);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return employee;
	}


}
