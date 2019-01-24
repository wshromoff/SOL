package com.jostens.jemm2.pojo;

import java.util.Objects;

public class Part
{

	private int ID = 0;
	private String name;
	private String designIDString;
	private String jostensIDString;
	private String partID;
	private String partIDDerivative;
	private String partValidation;
	private int designID;
	
	public Part()
	{
		
	}
	public Part(String exportString)
	{
		String splitString = exportString.replaceAll("\\|\\|", "| |");
		splitString = splitString.replaceAll("\\|\\|", "| |");
		String[] stringArr = splitString.split("\\|");
		if (stringArr.length != 6)
		{
			System.out.println("-->(" + stringArr.length + ") " + exportString);
			for (int i = 0; i < stringArr.length; i++)
			{
				System.out.println(" " + i + "   " + stringArr[i]);
			}
			return;
		}
		setName(stringArr[0]);
		setDesignIDString(emptyToNull(stringArr[1]));
		setJostensIDString(emptyToNull(stringArr[2]));
		setPartID(emptyToNull(stringArr[3]));
		setPartIDDerivative(emptyToNull(stringArr[4]));
		setPartValidation(emptyToNull(stringArr[5]));

		//  "CE602260|20180817-48c3dfa2babe|20180817-66b84ae756c2|CE602260||Asset Matches Part (Master)|"
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
	public String getDesignIDString()
	{
		return designIDString;
	}
	public void setDesignIDString(String designIDString)
	{
		this.designIDString = designIDString;
	}
	public String getJostensIDString()
	{
		return jostensIDString;
	}
	public void setJostensIDString(String jostensIDString)
	{
		this.jostensIDString = jostensIDString;
	}
	public String getPartID()
	{
		return partID;
	}
	public void setPartID(String partID)
	{
		this.partID = partID;
	}
	public String getPartIDDerivative()
	{
		return partIDDerivative;
	}
	public void setPartIDDerivative(String partIDDerivative)
	{
		this.partIDDerivative = partIDDerivative;
	}
	public String getPartValidation()
	{
		return partValidation;
	}
	public void setPartValidation(String partValidation)
	{
		this.partValidation = partValidation;
	}
	public int getDesignID()
	{
		return designID;
	}
	public void setDesignID(int designID)
	{
		this.designID = designID;
	}
	
	public boolean equals(Object obj)
	{
		Part part = (Part)obj;
//		System.out.println("HELLO " + getDisplayedInitials());
//		System.out.println("HELLO " + design.getDisplayedInitials());

		if (getID() != part.getID())
		{
			return false;
		}
		if (!Objects.equals(getName(), part.getName())) { return false; }
		if (!Objects.equals(getDesignIDString(), part.getDesignIDString())) { return false; }
		if (!Objects.equals(getJostensIDString(), part.getJostensIDString())) { return false; }
		if (!Objects.equals(getPartID(), part.getPartID())) { return false; }
		if (!Objects.equals(getPartIDDerivative(), part.getPartIDDerivative())) { return false; }
		if (!Objects.equals(getPartValidation(), part.getPartValidation())) { return false; }
		if (getDesignID() != part.getDesignID())
		{
			return false;
		}
	
		
		return true;

	}

}
