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
	}

	@Override
	public Purchase retrieve(Long id) throws SQLException, DAOException
	{
		return null;
	}

	@Override
	public void update(Purchase purchase) throws SQLException, DAOException
	{
	}

	@Override
	public void delete(Long id) throws SQLException, DAOException
	{
	}

	@Override
	public List<Purchase> retrieveForCustomerID(Long customerID) throws SQLException, DAOException
	{
		return null;
	}

	@Override
	public PurchaseSummary retrievePurchaseSummary(Long customerID) throws SQLException, DAOException
	{
		return null;
	}

	@Override
	public List<Purchase> retrieveForProductID(Long productID) throws SQLException, DAOException
	{
		return null;
	}
}
