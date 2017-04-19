package basicJPA;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import basicJPA.entity.Department;
import basicJPA.entity.Employee;

public class DepartmentServiceImpl implements DepartmentService
{
	@PersistenceContext 
	public EntityManager em; 

	@Override
	public void create(Department department)
	{
		try {
			em.getTransaction().begin();
			em.persist(department);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Department find(Long id)
	{
		try {
			em.getTransaction().begin();
			Department dept = em.find(Department.class, id);
			em.getTransaction().commit();
			return dept;
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Department findByName(String deptName) throws DAOException
	{
		em.getTransaction().begin();
		Department dept = (Department)em.createQuery("from Department as d where d.name = :name")
				.setParameter("name", deptName)
				.getSingleResult();
		em.getTransaction().commit();
		
		if(dept == null) {
			throw new DAOException("Department Name Not Found " + deptName);
		}
		
		return dept;
	}

	@Override
	public void update(Department departement)
	{
		try {
			em.getTransaction().begin();
			Department d2 = em.find(Department.class, departement.getId());
			d2.setName(departement.getName());
			d2.setDescription(departement.getDescription());
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void addEmpToDept(String deptName, Employee emp) throws DAOException
	{
		em.getTransaction().begin();
		Department dept = (Department)em.createQuery("from Department as d where d.name = :name")
				.setParameter("name", deptName)
				.getSingleResult();

		if(dept == null) {
			em.getTransaction().rollback();
			throw new DAOException("Department Name Not Found " + deptName);
		}
		
		dept.addEmployee(emp);
		em.getTransaction().commit();
	}

	@Override
	public void removeEmpFromDept(String deptName, Employee emp) throws DAOException
	{
		em.getTransaction().begin();
		Department dept = (Department)em.createQuery("from Department as d where d.name = :name")
				.setParameter("name", deptName)
				.getSingleResult();
		
		if(dept == null) {
			em.getTransaction().rollback();
			throw new DAOException("Department Name Not Found " + deptName);
		}
		
		dept.removeEmployee(emp);
		em.getTransaction().commit();
	}

}
