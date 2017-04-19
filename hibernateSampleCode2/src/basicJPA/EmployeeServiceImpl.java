package basicJPA;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import basicJPA.entity.Employee;

public class EmployeeServiceImpl implements EmployeeService
{
	@PersistenceContext 
	public EntityManager em; 

	@Override
	public void create(Employee employee)
	{
		try {
			em.getTransaction().begin();
			em.persist(employee);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Employee find(Long id)
	{
		try {
			em.getTransaction().begin();
			Employee employee = em.find(Employee.class, id);
			em.getTransaction().commit();
			return employee;
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void update(Employee employee)
	{
		try {
			em.getTransaction().begin();
			Employee e2 = em.find(Employee.class, employee.getId());
			e2.setName(employee.getName());
			e2.setSalary(employee.getSalary());
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public List<Employee> retrieveEmpByDeptAndSalary(String deptName, double salary)
	{
		em.getTransaction().begin();
		List<Employee> emps = (List<Employee>)em.createQuery("from Employee as e where e.salary >= :salary and e.department.name = :name")
				.setParameter("salary", salary)
				.setParameter("name", deptName)
				.getResultList();
		em.getTransaction().commit();
		return emps;
	}
}
