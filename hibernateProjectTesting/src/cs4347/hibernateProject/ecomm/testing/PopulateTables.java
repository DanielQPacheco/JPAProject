package cs4347.hibernateProject.ecomm.testing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import cs4347.hibernateProject.ecomm.entity.Address;
import cs4347.hibernateProject.ecomm.entity.CreditCard;
import cs4347.hibernateProject.ecomm.entity.Customer;
import cs4347.hibernateProject.ecomm.entity.Product;
import cs4347.hibernateProject.ecomm.entity.Purchase;

public class PopulateTables
{
	private File custFile;
	private File addrFile;
	private File ccardFile;
	private File prodFile;

	private void initialize()
	{
		custFile = new File("csvData/customers.csv");
		addrFile = new File("csvData/addresses.csv");
		ccardFile = new File("csvData/creditCards.csv");
		prodFile = new File("csvData/products.csv");
	}

	public static void main(String args[])
	{
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-simple_company");
		EntityManager em = null;
		
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			
			PopulateTables app = new PopulateTables();
			app.initialize();

			Map<Long, Customer> custMap = app.buildCustomers();
			app.addAddress(custMap);
			app.addCreditCards(custMap);
			System.out.println("Finished building customers: " + custMap.size());
			for (Customer customer : custMap.values()) {
				em.persist(customer);
			}
			System.out.println("Finished inserting customers");

			Map<Long, Product> products = app.buildProducts();
			System.out.println("Finished building products: " + products.size());
			for (Product product : products.values()) {
				em.persist(product);
			}
			System.out.println("Finished inserting products");

			List<Purchase> purchases = app.buildPurchases(custMap.values(), products.values().toArray(new Product[0]));
			System.out.println("Finished building purchases: " + purchases.size());
			for (Purchase purchase : purchases) {
				em.persist(purchase);
			}
			System.out.println("Finished inserting purchases");
			em.getTransaction().commit();
			System.out.println("Finished Initializing Database");			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			if(em != null) {
				em.getTransaction().rollback();
			}
		}
		finally {
			if(em != null) {
				em.close();
			}
			emf.close();
		}
	}

	private List<Purchase> buildPurchases(Collection<Customer> customers, Product[] products)
	{
		Random random = new Random();
		List<Purchase> result = new ArrayList<Purchase>();
		for (Customer customer : customers) {
			for (int idx = 0; idx < 10; idx++) {
				Product product = products[random.nextInt(products.length)];
				Purchase purchase = buildPurchase(customer, product);
				result.add(purchase);
			}
		}
		return result;
	}

	Random rnGen = new Random();

	private Purchase buildPurchase(Customer cust, Product prod)
	{
		Purchase purchase = new Purchase();
		purchase.setCustomer(cust);
		purchase.setProduct(prod);

		double purchaseAmount = rnGen.nextDouble() * 100.0;
		purchase.setPurchaseAmount(purchaseAmount);

		Calendar cal = GregorianCalendar.getInstance();
		int dateOffset = rnGen.nextInt(10 * 365);
		cal.add(Calendar.DAY_OF_YEAR, dateOffset);
		purchase.setPurchaseDate(new java.sql.Date(cal.getTimeInMillis()));

		return purchase;
	}

	private void addCreditCards(Map<Long, Customer> custMap) throws Exception
	{
		FileReader fr = new FileReader(ccardFile);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		while ((line = br.readLine()) != null) {
			Object items[] = parseCreditCard(line);
			Customer cust = custMap.get(items[0]);
			cust.setCreditCard((CreditCard) items[1]);
		}
		br.close();
	}

	private void addAddress(Map<Long, Customer> custMap) throws Exception
	{
		FileReader fr = new FileReader(addrFile);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		while ((line = br.readLine()) != null) {
			Object items[] = parseAddress(line);
			Customer cust = custMap.get(items[0]);
			cust.setAddress((Address) items[1]);
		}
		br.close();
	}

	private Map<Long, Product> buildProducts() throws Exception
	{
		Map<Long, Product> prodMap = new HashMap<Long, Product>();
		FileReader fr = new FileReader(prodFile);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		while ((line = br.readLine()) != null) {
			Object item[] = parseProduct(line);
			prodMap.put((Long) item[0], (Product) item[1]);
		}
		br.close();
		return prodMap;
	}

	private Map<Long, Customer> buildCustomers() throws Exception
	{
		Map<Long, Customer> custMap = new HashMap<Long, Customer>();
		FileReader fr = new FileReader(custFile);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		while ((line = br.readLine()) != null) {
			Object item[] = parseCustomer(line);
			custMap.put((Long) item[0], (Customer) item[1]);
		}
		br.close();
		return custMap;
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private Object[] parseCustomer(String line) throws ParseException
	{
		StringTokenizer st = new StringTokenizer(line, ",");
		Customer customer = new Customer();
		Long id = Long.parseLong(st.nextToken());
		customer.setFirstName(st.nextToken());
		customer.setLastName(st.nextToken());
		Character gender = ((String) st.nextToken()).charAt(0);
		customer.setGender(gender);
		Date dob = new java.sql.Date(sdf.parse(st.nextToken()).getTime());
		customer.setDob(dob);
		customer.setEmail(st.nextToken());

		Object[] result = { id, customer };
		return result;
	}

	private Object[] parseProduct(String line) throws ParseException
	{
		StringTokenizer st = new StringTokenizer(line, ",");

		Product product = new Product();
		Long id = Long.parseLong(st.nextToken());
		product.setProdName(st.nextToken());
		product.setProdDescription(st.nextToken());
		product.setProdCategory(Integer.parseInt(st.nextToken()));
		product.setProdUPC(st.nextToken());

		Object[] result = { id, product };
		return result;
	}

	private Object[] parseAddress(String line)
	{
		StringTokenizer st = new StringTokenizer(line, ",");

		Address address = new Address();
		address.setAddress1(st.nextToken());
		address.setCity(st.nextToken());
		address.setState(st.nextToken());
		address.setZipcode(st.nextToken());

		Long id = Long.parseLong(st.nextToken());
		return new Object[] { id, address };
	}

	private Object[] parseCreditCard(String line)
	{
		StringTokenizer st = new StringTokenizer(line, ",");

		CreditCard ccard = new CreditCard();
		ccard.setName(st.nextToken());
		ccard.setCcNumber(st.nextToken());
		ccard.setExpDate(st.nextToken());
		ccard.setSecurityCode(st.nextToken());

		Long id = Long.parseLong(st.nextToken());
		return new Object[] { id, ccard };
	}

}
