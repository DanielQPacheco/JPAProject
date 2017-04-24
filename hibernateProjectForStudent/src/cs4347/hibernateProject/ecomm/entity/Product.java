package cs4347.hibernateProject.ecomm.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Product")
public class Product 
{
	private Long id;
	private String prodName;
	private String prodDescription;
	private int prodCategory;
	private String prodUPC;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getProdName()
	{
		return prodName;
	}

	public void setProdName(String prodName)
	{
		this.prodName = prodName;
	}

	public String getProdDescription()
	{
		return prodDescription;
	}

	public void setProdDescription(String prodDescription)
	{
		this.prodDescription = prodDescription;
	}

	public int getProdCategory()
	{
		return prodCategory;
	}

	public void setProdCategory(int prodCategory)
	{
		this.prodCategory = prodCategory;
	}

	public String getProdUPC()
	{
		return prodUPC;
	}

	public void setProdUPC(String prodUPC)
	{
		this.prodUPC = prodUPC;
	}

}