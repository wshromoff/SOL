package com.wassoftware.solr;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

public class ProductBean
{

	private String id;
	private String name;
	private String price;
	private String contentType = "product";
	private List<String> keywords = new ArrayList<String>();
	private List<SubBean> subBeans = new ArrayList<SubBean>();

	public ProductBean()
	{
		
	}
	public ProductBean(String id, String name, String price)
	{
		this.id = id;
		this.name = name;
		this.price = price;
	}

	
	public String getContentType()
	{
		return contentType;
	}
	@Field("contentType")
	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}


	@Field("id")
	protected void setId(String id)
	{
		this.id = id;
	}

	@Field("name")
	protected void setName(String name)
	{
		this.name = name;
	}

	@Field("price")
	protected void setPrice(String price)
	{
		this.price = price;
	}

	public String getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
	public String getPrice()
	{
		return price;
	}

	public List<String> getKeywords()
	{
		return keywords;
	}
	@Field("keywords")
	public void setKeywords(List<String> keywords)
	{
		this.keywords = keywords;
	}
	public void addKeyword(String keyword)
	{
		keywords.add(keyword);
	}

	public List<SubBean> getSubBeans()
	{
		return subBeans;
	}
	@Field("subBeans")
	public void setSubBeans(List<SubBean> subBeans)
	{
		this.subBeans = subBeans;
	}
	public void addSubBean(SubBean subBean)
	{
		subBeans.add(subBean);
	}

	
}