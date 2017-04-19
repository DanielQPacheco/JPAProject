package basicJPA;

import basicJPA.entity.Department;
import basicJPA.entity.Employee;

public interface DepartmentService
{
	void create(Department department);
	Department find(Long id);
	Department findByName(String name) throws DAOException;
	void update(Department departement);
	void addEmpToDept(String deptName, Employee emp) throws DAOException;
	void removeEmpFromDept(String deptName, Employee emp) throws DAOException;
}
