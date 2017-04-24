package cs4347.hibernateProject.ecomm.services.impl;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//import basicJPA.entity.Department;
//import basicJPA.entity.Department;
import cs4347.hibernateProject.ecomm.entity.Product;
import cs4347.hibernateProject.ecomm.services.ProductPersistenceService;
import cs4347.hibernateProject.ecomm.util.DAOException;

public class ProductPersistenceServiceImpl implements ProductPersistenceService
{
	@PersistenceContext 
	public EntityManager em; 
		
	@Override
	public void create(Product product) throws SQLException, DAOException
	{
		try
		{
			em.getTransaction().begin();
			em.persist(product);
			em.getTransaction().commit();
		}
		catch (Exception ex)
		{
			em.getTransaction().rollback();
			throw ex; 
		}
		
		
	}

	@Override
	public Product retrieve(Long id) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		Product prod = (Product)em.createQuery("from Product as p where p.id = :id")
				.setParameter("id", id)
				.getSingleResult();
		em.getTransaction().commit();
		
		if(prod == null) {
			throw new DAOException("Product Name Not Found " + id);
		}
		
		return prod;
		
	}

	@Override
	public void update(Product product) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Product p2 = em.find(Product.class, product.getId());
			p2.setProdName(product.getProdName());
			p2.setProdDescription(product.getProdDescription());
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void delete(Long id) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		Product prod = (Product)em.createQuery("from Product as p where p.id = :id")
				.setParameter("id", id)
				.getSingleResult();
		
		if(prod == null) {
			em.getTransaction().rollback();
			throw new DAOException("Product Name Not Found " + id);
		}
		
		
		em.getTransaction().commit();
	}
	

	@Override
	public Product retrieveByUPC(String upc) throws SQLException, DAOException
	{
		return null;
	}

	@Override
	public List<Product> retrieveByCategory(int category) throws SQLException, DAOException
	{
		return null;
	}

}
