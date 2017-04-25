package cs4347.hibernateProject.ecomm.services.impl;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cs4347.hibernateProject.ecomm.entity.Purchase;
import cs4347.hibernateProject.ecomm.services.PurchasePersistenceService;
import cs4347.hibernateProject.ecomm.services.PurchaseSummary;
import cs4347.hibernateProject.ecomm.util.DAOException;

public class PurchasePersistenceServiceImpl implements PurchasePersistenceService
{
	@PersistenceContext 
	public EntityManager em; 
		
	@Override
	public void create(Purchase purchase) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			em.persist(purchase);
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public Purchase retrieve(Long id) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Purchase purchase = em.find(Purchase.class, id);
			em.getTransaction().commit();
			return purchase;
		}
		catch (Exception ex) {
			em.getTransaction().rollback();
			throw ex;
		}
	}

	@Override
	public void update(Purchase p1) throws SQLException, DAOException
	{
		try {
			em.getTransaction().begin();
			Purchase p2 = em.find(Purchase.class, p1.getId());
			p2.setPurchaseDate(p1.getPurchaseDate());
			p2.setPurchaseAmount(p1.getPurchaseAmount());
			p2.setCustomer(p1.getCustomer());
			p2.setProduct(p1.getProduct());
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
	}

	@Override
	public List<Purchase> retrieveForCustomerID(Long customerID) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		List<Purchase> purc = (List<Purchase>)em.createQuery("from Purchase as p where p.customer.id = :customerID")
			.setParameter("customerID", customerID)
			.getSingleResult();
		em.getTransaction().commit();
		
		if(purc == null) {
			throw new DAOException("Purchase by Customer " + customerID + " not found.");
		}
		
		return purc;
	}

	@Override
	public PurchaseSummary retrievePurchaseSummary(Long customerID) throws SQLException, DAOException
	{
		return null;
	}

	@Override
	public List<Purchase> retrieveForProductID(Long productID) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		List<Purchase> purc = (List<Purchase>)em.createQuery("from Purchase as p where p.product.id = :productID")
			.setParameter("productID", productID)
			.getSingleResult();
		em.getTransaction().commit();
		
		if(purc == null) {
			throw new DAOException("Purchase with productID " + productID + " not found.");
		}
		
		return purc;
	}
}
