package cs4347.hibernateProject.ecomm.services.impl;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
	}

	@Override
	public Product retrieve(Long id) throws SQLException, DAOException
	{
		return null;
	}

	@Override
	public void update(Product product) throws SQLException, DAOException
	{
	}

	@Override
	public void delete(Long id) throws SQLException, DAOException
	{
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
