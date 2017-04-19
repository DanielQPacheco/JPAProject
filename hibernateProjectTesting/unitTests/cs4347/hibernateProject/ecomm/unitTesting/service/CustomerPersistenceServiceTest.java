package cs4347.hibernateProject.ecomm.unitTesting.service;

import static org.junit.Assert.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import cs4347.hibernateProject.ecomm.entity.Address;
import cs4347.hibernateProject.ecomm.entity.CreditCard;
import cs4347.hibernateProject.ecomm.entity.Customer;
import cs4347.hibernateProject.ecomm.services.impl.CustomerPersistenceServiceImpl;

public class CustomerPersistenceServiceTest
{
	protected static EntityManagerFactory emf;
	
	@BeforeClass
    public static void createEntityManagerFactory() {
        emf = Persistence.createEntityManagerFactory("jpa-simple_company");
    }

    @AfterClass
    public static void closeEntityManagerFactory() {
        emf.close();
    }
    
	@Test
	public void testCreate() throws Exception
	{
		CustomerPersistenceServiceImpl cpService = new CustomerPersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		cpService.em = em;
		
		Customer cust = buildCustomer();
		assertNull(cust.getId());
		cpService.create(cust);
		assertNotNull(cust.getId());

		Customer cust3 = em.find(Customer.class, cust.getId());
		assertNotNull(cust3);
		assertTrue(cust3 == cust);
		
		em.close();
	}

	@Test
	public void testRetrieve() throws Exception
	{
		CustomerPersistenceServiceImpl cpService = new CustomerPersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		cpService.em = em;

		Customer cust = buildCustomer();
		cpService.create(cust);
		Long id = cust.getId();

		Customer cust3 = cpService.retrieve(id);
		assertNotNull(cust3);
		assertEquals(cust.getId(), cust3.getId());
		assertEquals(cust.getFirstName(), cust3.getFirstName());
		assertEquals(cust.getLastName(), cust3.getLastName());
		assertTrue(cust.getDob().equals(cust3.getDob()));
		assertEquals(cust.getGender(), cust3.getGender());
		assertEquals(cust.getEmail(), cust3.getEmail());
		
		Address addr1 = cust.getAddress();
		Address addr2 = cust3.getAddress();
		assertNotNull(addr2);
		assertEquals(addr1.getAddress1(), addr2.getAddress1());
		assertEquals(addr1.getAddress2(), addr2.getAddress2());
		assertEquals(addr1.getCity(), addr2.getCity());
		assertEquals(addr1.getState(), addr2.getState());
		assertEquals(addr1.getZipcode(), addr2.getZipcode());

		CreditCard ccard = cust.getCreditCard();
		CreditCard ccard2 = cust3.getCreditCard();
		assertNotNull(ccard2);
		assertEquals(ccard.getName(), ccard2.getName());
		assertEquals(ccard.getCcNumber(), ccard2.getCcNumber());
		assertEquals(ccard.getExpDate(), ccard2.getExpDate());
		assertEquals(ccard.getSecurityCode(), ccard2.getSecurityCode());
		
		em.close();
}

	@Test
	public void testUpdate() throws Exception
	{
		CustomerPersistenceServiceImpl cpService = new CustomerPersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		cpService.em = em;

		Customer cust = buildCustomer();
		cpService.create(cust);
		Long id = cust.getId();
		
		String newEmail = "fred@gmail" + System.currentTimeMillis();
		cust.setEmail(newEmail);
		Address addr = cust.getAddress();
		addr.setZipcode("00001");
		CreditCard ccard = cust.getCreditCard();
		ccard.setExpDate("01/1234");
		
		cpService.update(cust);
		em.close();
		
		em = emf.createEntityManager();
		cpService.em = em;

		Customer cust3 = cpService.retrieve(id);
		assertEquals(newEmail, cust3.getEmail());
		Address addr2 = cust3.getAddress();
		assertEquals("00001", addr2.getZipcode());
		CreditCard ccard2 = cust3.getCreditCard();
		assertEquals("01/1234", ccard2.getExpDate());
		
		em.close();
	}

	@Test
	public void testDelete() throws Exception
	{
		CustomerPersistenceServiceImpl cpService = new CustomerPersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		cpService.em = em;

		Customer cust = buildCustomer();
		cpService.create(cust);
		Long custID = cust.getId();
		
		cpService.delete(custID);
		
		assertNull(em.find(Customer.class, custID));

		// This code assumes that Address.customerID FK is set cascade delete
		assertNull(em.find(Address.class, cust.getAddress().getId()));

		// This code assumes that credit_card.customerID FK is set cascade delete
		assertNull(em.find(CreditCard.class, cust.getCreditCard().getId()));

		em.close();
	}

	@Test
	public void testRetrieveByZipCode() throws Exception
	{
		CustomerPersistenceServiceImpl cpService = new CustomerPersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		cpService.em = em;

		List<Customer> custs = cpService.retrieveByZipCode("76953-7323"); // Need a customer with this address
		assertTrue(custs.size() > 0);

		for(Customer customer: custs) {
			assertTrue(customer.getAddress() != null);
			assertTrue(customer.getCreditCard() != null);
		}
		
		em.close();
	}

	@Test
	public void testRetrieveByDOB() throws Exception
	{
		CustomerPersistenceServiceImpl cpService = new CustomerPersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		cpService.em = em;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date start = new Date(sdf.parse("1970-01-01").getTime());
		Date end = new Date(sdf.parse("1990-01-01").getTime());
		
		List<Customer> custs = cpService.retrieveByDOB(start, end); // Need a customer with this address
		assertTrue(custs.size() > 0);
		
		for(Customer customer: custs) {
			assertTrue(customer.getAddress() != null);
			assertTrue(customer.getCreditCard() != null);
		}
		
		em.close();
	}

	private Customer buildCustomer()
	{
		Customer cust = new Customer();
		cust.setFirstName("fred");
		cust.setLastName("flintstone");
		cust.setDob(new java.sql.Date(System.currentTimeMillis()));
		cust.setEmail("fred@gmail" + System.currentTimeMillis());
		cust.setGender('M');

		Address addr = new Address();
		addr.setAddress1("123 First St.");
		addr.setAddress2("Appmt 1b");
		addr.setCity("Dallas");
		addr.setState("TX");
		addr.setZipcode("70765");
		cust.setAddress(addr);

		CreditCard ccard = new CreditCard();
		ccard.setName("Fred Flintstone");
		ccard.setCcNumber("1111 1111 1111 1111");
		ccard.setExpDate("12/2018");
		ccard.setSecurityCode("123");
		cust.setCreditCard(ccard);

		return cust;
	}
}
