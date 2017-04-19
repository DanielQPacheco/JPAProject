package cs4347.hibernateProject.ecomm.services;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import cs4347.hibernateProject.ecomm.entity.Customer;
import cs4347.hibernateProject.ecomm.util.DAOException;

/** 
 */
public interface CustomerPersistenceService
{
	void create(Customer customer) throws SQLException, DAOException;
	Customer retrieve(Long id) throws SQLException, DAOException;
	void update(Customer customer) throws SQLException, DAOException;
	void delete(Long id) throws SQLException, DAOException;
	
	/**
	 * Retrieve all Customers whose address is in the given zipcode.
	 */
	List<Customer> retrieveByZipCode(String zipCode) throws SQLException, DAOException;
	
	/**
	 * Retrieve all Customer whose DOB is in the given date range.
	 */
	List<Customer> retrieveByDOB(Date startDate, Date endDate) throws SQLException, DAOException;
}