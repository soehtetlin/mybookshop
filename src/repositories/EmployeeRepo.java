package repositories;

import entities.Employee;

import java.util.List;

public interface EmployeeRepo {

	void createEmployee(Employee employee);

	void updateEmployee(String id, Employee employee);

	void blockEmployee(String id);

	List<Employee> findAllEmployees();

	Employee findEmployeeById(String id);
}
