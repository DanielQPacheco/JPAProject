package cs4347.hibernateProject.ecomm.services;

import java.sql.SQLException;
import java.util.List;

import cs4347.hibernateProject.ecomm.entity.Product;
import cs4347.hibernateProject.ecomm.util.DAOException;

/** 
 */
public interface ProductPersistenceService
{
	void create(Product product) throws SQLException, DAOException;
	Product retrieve(Long id) throws SQLException, DAOException;
	void update(Product product) throws SQLException, DAOException;
	void delete(Long id) throws SQLException, DAOException;
	
	/**
	 * Retrieve a product by its unique UPC
	 */
	Product retrieveByUPC(String upc) throws SQLException, DAOException;

	/**
	 * Retrive products in the given category
	 */
	List<Product> retrieveByCategory(int category) throws SQLException, DAOException;
	
}