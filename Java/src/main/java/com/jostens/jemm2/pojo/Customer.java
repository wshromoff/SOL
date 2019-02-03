package com.jostens.jemm2.pojo;

import java.util.Objects;

import com.jostens.jemm2.solr.PartDocument;

public class Customer
{
	private int ID = 0;
	private String name;
	private String customerID;
	private String city;
	private String state;
	private String color1;
	private String color2;
	private String color3;
	private String mascot;
	
	// Cumberland Valley Christian School (Chambersburg, PA)|1045476|Chambersburg|Pennsylvania|US|Red|White|NA|Blazers|
	public Customer()
	{
		
	}
	public Customer(String exportString)
	{
		String splitString = exportString.replaceAll("\\|\\|", "| |");
		splitString = splitString.replaceAll("\\|\\|", "| |");
		String[] stringArr = splitString.split("\\|");
		if (stringArr.length != 9)
		{
			System.out.println("-->(" + stringArr.length + ") " + exportString);
			for (int i = 0; i < stringArr.length; i++)
			{
				System.out.println(" " + i + "   " + stringArr[i]);
			}
			return;
		}
		setName(stringArr[0]);
		setCustomerID(emptyToNull(stringArr[1]));
		setCity(emptyToNull(stringArr[2]));
		setState(emptyToNull(stringArr[3]));
		setColor1(emptyToNull(stringArr[5]));
		setColor2(emptyToNull(stringArr[6]));
		setColor3(emptyToNull(stringArr[7]));
		setMascot(emptyToNull(stringArr[8]));

	}

	private String emptyToNull(String strValue)
	{
		if (strValue == null || strValue.trim().length() == 0)
		{
			return null;
		}
		return strValue;
	}
	
	public int getID()
	{
		return ID;
	}
	public void setID(int iD)
	{
		ID = iD;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getCustomerID()
	{
		return customerID;
	}
	public void setCustomerID(String customerID)
	{
		this.customerID = customerID;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city = city;
	}
	public String getState()
	{
		return state;
	}
	public void setState(String state)
	{
		this.state = state;
	}
	public String getColor1()
	{
		return color1;
	}
	public void setColor1(String color1)
	{
		this.color1 = color1;
	}
	public String getColor2()
	{
		return color2;
	}
	public void setColor2(String color2)
	{
		this.color2 = color2;
	}
	public String getColor3()
	{
		return color3;
	}
	public void setColor3(String color3)
	{
		this.color3 = color3;
	}
	public String getMascot()
	{
		return mascot;
	}
	public void setMascot(String mascot)
	{
		this.mascot = mascot;
	}

	public boolean equals(Object obj)
	{
		Customer customer = (Customer)obj;

		if (getID() != customer.getID())
		{
			return false;
		}
		if (!Objects.equals(getName(), customer.getName())) { return false; }
		if (!Objects.equals(getCustomerID(), customer.getCustomerID())) { return false; }
		if (!Objects.equals(getCity(), customer.getCity())) { return false; }
		if (!Objects.equals(getState(), customer.getState())) { return false; }
		if (!Objects.equals(getColor1(), customer.getColor1())) { return false; }
		if (!Objects.equals(getColor2(), customer.getColor2())) { return false; }
		if (!Objects.equals(getColor3(), customer.getColor3())) { return false; }
		if (!Objects.equals(getMascot(), customer.getMascot())) { return false; }
			
		return true;

	}

	/**
	 * Return a PartDocument representative of this Part for addition to SOLR
	 */
//	public PartDocument getPartDocument()
//	{
//		PartDocument pd = new PartDocument();
//		pd.setDatabaseID(getID());
//		pd.setName(getName());
//		pd.setPartValidation(getPartValidation());
//		pd.setDesignID("DS_" + pd.getFormattedID(designID));
//		
//		return pd;
//	}

	
}
