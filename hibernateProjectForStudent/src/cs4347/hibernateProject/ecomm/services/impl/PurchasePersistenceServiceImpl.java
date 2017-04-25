package cs4347.hibernateProject.ecomm.services.impl;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cs4347.hibernateProject.ecomm.entity.Product;
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
		em.getTransaction().begin();
		Purchase purc = (Purchase)em.createQuery("from Purchase as p where p.id = :id")
				.setParameter("id", id)
				.getSingleResult();
		
		if(purc == null) {
			em.getTransaction().rollback();
			throw new DAOException("Purchase Not Found " + id);
		}
		
		em.remove(purc);
		em.getTransaction().commit();
	}

	@Override
	public List<Purchase> retrieveForCustomerID(Long customerID) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		List<Purchase> purc = (List<Purchase>)em.createQuery("from Purchase as p where p.customer.id = :customerID")
			.setParameter("customerID", customerID)
			.getResultList();
		em.getTransaction().commit();
		
		if(purc == null) {
			throw new DAOException("Purchase by Customer " + customerID + " not found.");
		}
		
		return purc;
	}

	@Override
	public PurchaseSummary retrievePurchaseSummary(Long customerID) throws SQLException, DAOException
	{
		double min=-1;
		double max=-1;
		double avg=-1;
		em.getTransaction().begin();
		List<Purchase> ps = (List<Purchase>)em.createQuery("from Purchase as p where p.customer.id = :customerID")
		.setParameter("customerID", customerID)
		.getResultList();
		em.getTransaction().commit();

		if(ps == null) {
		throw new DAOException("Purchase by Customer " + customerID + " not found.");
		}
		else{
		min = ps.get(0).getPurchaseAmount();
		max = ps.get(0).getPurchaseAmount();
		avg = 0;
		double sum = 0;
		for(int i = 0; i < ps.size(); i++){
		if(ps.get(i).getPurchaseAmount() < min)
		min = ps.get(1).getPurchaseAmount();
		if(ps.get(i).getPurchaseAmount() > max)
		max = ps.get(1).getPurchaseAmount();
		sum+=ps.get(i).getPurchaseAmount();
		}
		avg = sum/ps.size();
		}
		PurchaseSummary pursumm = new PurchaseSummary();
		pursumm.avgPurchase = avg;
		pursumm.minPurchase = min;
		pursumm.maxPurchase = max;
		return pursumm;
	}

	@Override
	public List<Purchase> retrieveForProductID(Long productID) throws SQLException, DAOException
	{
		em.getTransaction().begin();
		List<Purchase> purc = (List<Purchase>)em.createQuery("from Purchase as p where p.product.id = :productID")
			.setParameter("productID", productID)
			.getResultList();
		em.getTransaction().commit();
		
		if(purc == null) {
			throw new DAOException("Purchase with productID " + productID + " not found.");
		}
		
		return purc;
	}
}