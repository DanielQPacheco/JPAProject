package cs4347.hibernateProject.ecomm.services;

import java.sql.SQLException;
import java.util.List;

import cs4347.hibernateProject.ecomm.entity.Purchase;
import cs4347.hibernateProject.ecomm.util.DAOException;

/** 
 * 
 */
public interface PurchasePersistenceService
{
	void create(Purchase purchase) throws SQLException, DAOException;
	Purchase retrieve(Long id) throws SQLException, DAOException;
	void update(Purchase purchase) throws SQLException, DAOException;
	void delete(Long id) throws SQLException, DAOException;
	
	/**
	 * Retrieve purchases made by the given customer.
	 */
	List<Purchase> retrieveForCustomerID(Long customerID) throws SQLException, DAOException;
	
	/**
	 * Produce a purchase summary report for the given customer.
	 */
	PurchaseSummary retrievePurchaseSummary(Long customerID) throws SQLException, DAOException;

	/**
	 * Retrieve purchases made for the given product.
	 */
	List<Purchase> retrieveForProductID(Long productID) throws SQLException, DAOException;
}