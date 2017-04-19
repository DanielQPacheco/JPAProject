package cs4347.hibernateProject.ecomm.unitTesting.service;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import cs4347.hibernateProject.ecomm.entity.Product;
import cs4347.hibernateProject.ecomm.services.impl.ProductPersistenceServiceImpl;

public class ProductPersistenceServiceTest
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
		ProductPersistenceServiceImpl ppService = new ProductPersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		ppService.em = em;

		Product product = buildProduct();
		assertNull(product.getId());
		ppService.create(product);
		assertNotNull(product.getId());

		em.close();
	}

	@Test
	public void testRetrieve() throws Exception
	{
		ProductPersistenceServiceImpl ppService = new ProductPersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		ppService.em = em;

		Product product = buildProduct();
		ppService.create(product);
		Long newProdID = product.getId();
		
		Product prod2 = ppService.retrieve(newProdID);
		assertNotNull(prod2);
		assertEquals(product.getProdCategory(), prod2.getProdCategory());
		assertEquals(product.getProdDescription(), prod2.getProdDescription());
		assertEquals(product.getProdName(), prod2.getProdName());
		assertEquals(product.getProdUPC(), prod2.getProdUPC());

		em.close();
	}

	@Test
	public void testUpdate() throws Exception
	{
		ProductPersistenceServiceImpl ppService = new ProductPersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		ppService.em = em;

		Product product = buildProduct();
		ppService.create(product);
		Long id = product.getId();
		
		String newUPC = "3333333333";
		product.setProdUPC(newUPC);
		ppService.update(product);

		Product prod3 = ppService.retrieve(id);
		assertEquals(product.getId(), prod3.getId());
		assertEquals(product.getProdName(), prod3.getProdName());
		assertEquals(newUPC, prod3.getProdUPC());
		em.close();
	}

	@Test
	public void testDelete() throws Exception
	{
		ProductPersistenceServiceImpl ppService = new ProductPersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		ppService.em = em;

		Product product = buildProduct();
		ppService.create(product);

		Long id = product.getId();
		ppService.delete(id);
		
		Product prod3 = ppService.retrieve(id);
		assertNull(prod3);
		em.close();
	}

	@Test
	public void testRetrieveByUPC() throws Exception
	{
		ProductPersistenceServiceImpl ppService = new ProductPersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		ppService.em = em;

		Product product = ppService.retrieveByUPC("576236786900");
		assertNotNull(product);
		em.close();
	}

	@Test
	public void testRetrieveByCategory() throws Exception
	{
		ProductPersistenceServiceImpl ppService = new ProductPersistenceServiceImpl();
		EntityManager em = emf.createEntityManager();
		ppService.em = em;

		List<Product> products = ppService.retrieveByCategory(2);
		assertTrue(products.size() > 1);
		em.close();
	}

	private Product buildProduct()
    {
		Product product = new Product();
		product.setProdCategory(1);
		product.setProdDescription("Product Description");
		product.setProdName("Product Name");
		product.setProdUPC("1112223333");
	    return product;
    }
}
