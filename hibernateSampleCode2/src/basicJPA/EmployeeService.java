package basicJPA;


import java.util.List;

import basicJPA.entity.Employee;

public interface EmployeeService
{
	void create(Employee employee);
	Employee find(Long id);
	void update(Employee employee);
	
	List<Employee>retrieveEmpByDeptAndSalary(String deptName, double salary);
}
