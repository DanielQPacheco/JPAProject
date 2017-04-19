package testCases;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import basicJPA.EmployeeServiceImpl;
import basicJPA.entity.Employee;

public class EmployeeServiceTC
{
	protected static EntityManagerFactory emf;

	@BeforeClass
	public static void createEntityManagerFactory()
	{
		emf = Persistence.createEntityManagerFactory("jpa-employee");
	}

	@AfterClass
	public static void closeEntityManagerFactory()
	{
		emf.close();
	}

	@Test
	public void testCreate()
	{
		EmployeeServiceImpl empService = new EmployeeServiceImpl();
		EntityManager em = emf.createEntityManager();
		empService.em = em;

		Employee emp = buildEmployee();
		assertNull(emp.getId());
		empService.create(emp);
		assertNotNull(emp.getId());

		Employee emp2 = em.find(Employee.class, emp.getId());
		assertNotNull(emp2);
		assertTrue(emp2 == emp);

		em.close();
	}

	@Test
	public void testFindEmployee()
	{
		EmployeeServiceImpl empService = new EmployeeServiceImpl();
		EntityManager em = emf.createEntityManager();
		empService.em = em;

		Employee emp = buildEmployee();
		empService.create(emp);
		Long id = emp.getId();

		Employee emp2 = empService.find(id);
		assertNotNull(emp2);
		assertEquals(emp.getId(), emp2.getId());
		assertEquals(emp.getName(), emp2.getName());
		assertEquals(emp.getSalary(), emp2.getSalary(), .01);

		em.close();
	}

	@Test
	public void testUpdateEmployee()
	{
		EmployeeServiceImpl empService = new EmployeeServiceImpl();
		EntityManager em = emf.createEntityManager();
		empService.em = em;

		Employee emp = buildEmployee();
		empService.create(emp);
		Long id = emp.getId();

		Long ts = System.currentTimeMillis();
		emp.setSalary(ts);

		empService.update(emp);
		em.close();

		em = emf.createEntityManager();
		empService.em = em;

		Employee emp2 = empService.find(id);
		assertEquals(ts, emp2.getSalary(), .01);

		em.close();
	}

	@Test
	public void testRetrieveEmpByDeptAndSalary()
	{
		EmployeeServiceImpl empService = new EmployeeServiceImpl();
		EntityManager em = emf.createEntityManager();
		empService.em = em;
		
		List<Employee> emps = empService.retrieveEmpByDeptAndSalary("OSI", 100000);
		assertTrue(!emps.isEmpty());
	}

	private Employee buildEmployee()
	{
		Employee emp = new Employee();
		Long ts = System.currentTimeMillis();
		emp.setName("Employee " + ts);
		emp.setSalary(ts);
		return emp;
	}

}
