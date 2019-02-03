package com.jostens.jemm2.solr;

import org.apache.solr.client.solrj.beans.Field;

public class CustomerDocument extends SOLRBase
{
	private String customerID;
	private String city;
	private String state;
	private String mascot;
	
	
	public String getCustomerID()
	{
		return customerID;
	}
	@Field
	public void setCustomerID(String customerID)
	{
		this.customerID = customerID;
	}
	public String getCity()
	{
		return city;
	}
	@Field
	public void setCity(String city)
	{
		this.city = city;
	}
	public String getState()
	{
		return state;
	}
	@Field
	public void setState(String state)
	{
		this.state = state;
	}
	public String getMascot()
	{
		return mascot;
	}
	@Field
	public void setMascot(String mascot)
	{
		this.mascot = mascot;
	}

	public String getContentTypeFull()
	{
		return "Customer";
	}

	public String getContentTypeAbbrev()
	{
		return "CU";
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString());
		sb.append("Customer ID: " + getCustomerID() + "\n");
		sb.append("City: " + getCity() + "\n");
		sb.append("State: " + getState() + "\n");
		sb.append("Mascot: " + getMascot() + "\n");
		
		return sb.toString();
	}

}
