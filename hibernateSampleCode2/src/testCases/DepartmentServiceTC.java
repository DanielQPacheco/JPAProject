package testCases;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import basicJPA.DepartmentServiceImpl;
import basicJPA.entity.Department;
import basicJPA.entity.Employee;

public class DepartmentServiceTC
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
		DepartmentServiceImpl deptService = new DepartmentServiceImpl();
		EntityManager em = emf.createEntityManager();
		deptService.em = em;

		Department dept = buildDepartment();
		assertNull(dept.getId());
		deptService.create(dept);
		assertNotNull(dept.getId());

		Department dept2 = em.find(Department.class, dept.getId());
		assertNotNull(dept2);
		assertTrue(dept == dept2);

		em.close();
	}

	@Test
	public void testFindDepartment()
	{
		DepartmentServiceImpl deptService = new DepartmentServiceImpl();
		EntityManager em = emf.createEntityManager();
		deptService.em = em;

		Department dept = buildDepartment();
		deptService.create(dept);
		assertNotNull(dept.getId());

		Department dept2 = deptService.find(dept.getId());
		assertNotNull(dept2);
		assertEquals(dept.getId(), dept2.getId());
		assertEquals(dept.getName(), dept2.getName());
		assertEquals(dept.getDescription(), dept2.getDescription());

		em.close();
	}

	@Test
	public void testUpdateDepartment()
	{
		DepartmentServiceImpl deptService = new DepartmentServiceImpl();
		EntityManager em = emf.createEntityManager();
		deptService.em = em;

		Department dept = buildDepartment();
		deptService.create(dept);
		assertNotNull(dept.getId());

		Long ts = System.currentTimeMillis();
		dept.setDescription("UpdatedDescription " + ts);

		deptService.update(dept);
		em.close();

		em = emf.createEntityManager();
		deptService.em = em;

		Department dept2 = deptService.find(dept.getId());
		assertEquals(dept.getDescription(), dept2.getDescription());

		em.close();
	}

	@Test
	public void testAddEmpToDept() throws Exception
	{
		DepartmentServiceImpl deptService = new DepartmentServiceImpl();
		EntityManager em = emf.createEntityManager();
		deptService.em = em;

		Department dept = deptService.findByName("OSI");
		int beforeSize = dept.getEmployees().size();

		Employee emp = buildEmployee();
		deptService.addEmpToDept("OSI", emp);
		
		Department dept2 = deptService.findByName("OSI");
		assertTrue(beforeSize < dept2.getEmployees().size());
	}

	@Test
	public void testRemoveEmpFromDept() throws Exception
	{
		DepartmentServiceImpl deptService = new DepartmentServiceImpl();
		EntityManager em = emf.createEntityManager();
		deptService.em = em;

		Department dept = deptService.findByName("OSI");
		int beforeSize = dept.getEmployees().size();

		Employee emp = (Employee)dept.getEmployees().toArray()[0];
		deptService.removeEmpFromDept("OSI", emp);
		
		Department dept2 = deptService.findByName("OSI");
		assertTrue(beforeSize > dept2.getEmployees().size());
	}

	private Department buildDepartment()
	{
		Department dept = new Department();
		Long ts = System.currentTimeMillis();
		dept.setName("DeptName " + ts);
		dept.setDescription("DeptDesc " + ts);
		return dept;
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
