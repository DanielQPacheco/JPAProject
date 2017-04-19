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

import cs4347.hibernateProject.ecomm.entity.Customer;
import cs4347.hibernateProject.ecomm.entity.Product;
import cs4347.hibernateProject.ecomm.entity.Purchase;
import cs4347.hibernateProject.ecomm.services.PurchaseSummary;
import cs4347.hibernateProject.ecomm.services.impl.PurchasePersistenceServiceImpl;

public class PurchasePersistenceServiceTest
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
    

	// Must be existing Customer and Product IDs
	// **** It is acceptable to modify the ID values for your testing 
	Long customerID = 2l;
	Long productID = 2l;

	@Test
	public void testCreate() throws Exception
	{
		PurchasePersistenceServiceImpl ppService = new PurchasePersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		ppService.em = em;
		
		Purchase purchase = buildPurchase(em, customerID, productID);
		assertNull(purchase.getId());
		ppService.create(purchase);
		assertNotNull(purchase.getId());
	}

	@Test
	public void testRetrieve() throws Exception
	{
		PurchasePersistenceServiceImpl ppService = new PurchasePersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		ppService.em = em;

		Purchase purchase = buildPurchase(em, customerID, productID);
		ppService.create(purchase);
		Long newID = purchase.getId();

		Purchase purch2 = ppService.retrieve(newID);
		assertNotNull(purch2);
		assertEquals(purchase.getId(), purch2.getId());
		assertEquals(purchase.getPurchaseAmount(), purch2.getPurchaseAmount(), 0.1);
		assertEquals(purchase.getCustomer().getId(), purch2.getCustomer().getId());
		assertEquals(purchase.getProduct().getId(), purch2.getProduct().getId());

		// Elminate time component from date comparison
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
		String d1 = formatter.format(purchase.getPurchaseDate());
		String d2 = formatter.format(purch2.getPurchaseDate());
		assertEquals(d1, d2);
	}

	@Test
	public void testUpdate() throws Exception
	{
		PurchasePersistenceServiceImpl ppService = new PurchasePersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		ppService.em = em;

		Purchase purchase = buildPurchase(em, customerID, productID);
		ppService.create(purchase);
		Long newID = purchase.getId();

		Double newPrice = 2222.00;
		Purchase purchase2 = ppService.retrieve(newID);
		purchase2.setPurchaseAmount(newPrice);
		ppService.update(purchase2);
		
		em.close();
		em = emf.createEntityManager();
		ppService.em = em;

		Purchase purchase3 = ppService.retrieve(newID);
		assertEquals(purchase2.getId(), purchase3.getId());
		assertEquals((double) newPrice, purchase.getPurchaseAmount(), 0.1);
		em.close();
	}

	@Test
	public void testDelete() throws Exception
	{
		PurchasePersistenceServiceImpl ppService = new PurchasePersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		ppService.em = em;

		Purchase purchase = buildPurchase(em, customerID, productID);
		ppService.create(purchase);

		Long id = purchase.getId();
		ppService.delete(id);

		em.close();
		em = emf.createEntityManager();
		ppService.em = em;
		
		Purchase purchase3 = ppService.retrieve(id);
		assertNull(purchase3);
	}

	@Test
	public void testRetrieveForCustomerID() throws Exception
	{
		PurchasePersistenceServiceImpl ppService = new PurchasePersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		ppService.em = em;

		List<Purchase> purchases = ppService.retrieveForCustomerID(customerID);
		assertTrue(purchases.size() > 0);
	}

	@Test
	public void testRetrievePurchaseSummary() throws Exception
	{
		PurchasePersistenceServiceImpl ppService = new PurchasePersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		ppService.em = em;

		PurchaseSummary summary = ppService.retrievePurchaseSummary(customerID);
		assertNotNull(summary);
		assertTrue(summary.avgPurchase > 0);
		assertTrue(summary.minPurchase > 0);
		assertTrue(summary.maxPurchase > 0);
	}

	@Test
	public void testRetrieveForProductID() throws Exception
	{
		PurchasePersistenceServiceImpl ppService = new PurchasePersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		ppService.em = em;

		List<Purchase> purchases = ppService.retrieveForProductID(productID);
		assertTrue(purchases.size() > 0);
	}

	private Purchase buildPurchase(EntityManager em, Long customerID, Long productID)
	{
		Customer customer = em.find(Customer.class, customerID);
		assertNotNull(customer);

		Product product = em.find(Product.class, productID);
		assertNotNull(product);
		
		Purchase purchase = new Purchase();
		purchase.setPurchaseAmount(100.00);
		purchase.setPurchaseDate(new Date(System.currentTimeMillis()));
		purchase.setCustomer(customer);
		purchase.setProduct(product);
		return purchase;
	}
}
