package com.jostens.jemm2.pojo;

import java.util.Objects;

import com.jostens.jemm2.solr.CustomerPackageDocument;

public class CustomerPackage
{

	private int ID = 0;
	private String name;
	private int packageID;
	private int customerID;
	private String affiliationByUse;
	private String historicUseColor;
	private String historicUseDesign;
	private String statusLifeCycle;
	private String statusCataloging;
	private String statusAutomation;
	private String statusAvailability;
	private String businessDefaultUse;
	
	private AssetPackage aPackage = null;
	
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
	public int getPackageID()
	{
		return packageID;
	}
	public void setPackageID(int packageID)
	{
		this.packageID = packageID;
	}
	public int getCustomerID()
	{
		return customerID;
	}
	public void setCustomerID(int customerID)
	{
		this.customerID = customerID;
	}
	public String getAffiliationByUse()
	{
		return affiliationByUse;
	}
	public void setAffiliationByUse(String affiliationByUse)
	{
		this.affiliationByUse = affiliationByUse;
	}
	public String getHistoricUseColor()
	{
		return historicUseColor;
	}
	public void setHistoricUseColor(String historicUseColor)
	{
		this.historicUseColor = historicUseColor;
	}
	public String getHistoricUseDesign()
	{
		return historicUseDesign;
	}
	public void setHistoricUseDesign(String historicUseDesign)
	{
		this.historicUseDesign = historicUseDesign;
	}
	public String getStatusLifeCycle()
	{
		return statusLifeCycle;
	}
	public void setStatusLifeCycle(String statusLifeCycle)
	{
		this.statusLifeCycle = statusLifeCycle;
	}
	public String getStatusCataloging()
	{
		return statusCataloging;
	}
	public void setStatusCataloging(String statusCataloging)
	{
		this.statusCataloging = statusCataloging;
	}
	public String getStatusAutomation()
	{
		return statusAutomation;
	}
	public void setStatusAutomation(String statusAutomation)
	{
		this.statusAutomation = statusAutomation;
	}
	public String getStatusAvailability()
	{
		return statusAvailability;
	}
	public void setStatusAvailability(String statusAvailability)
	{
		this.statusAvailability = statusAvailability;
	}
	public String getBusinessDefaultUse()
	{
		return businessDefaultUse;
	}
	public void setBusinessDefaultUse(String businessDefaultUse)
	{
		this.businessDefaultUse = businessDefaultUse;
	}
	
	public boolean equals(Object obj)
	{
		CustomerPackage aCustomerPackage = (CustomerPackage)obj;

		if (getID() != aCustomerPackage.getID())
		{
			return false;
		}
		if (getPackageID() != aCustomerPackage.getPackageID())
		{
			return false;
		}
		if (getCustomerID() != aCustomerPackage.getCustomerID())
		{
			return false;
		}
		
		if (!Objects.equals(getName(), aCustomerPackage.getName())) { return false; }
		if (!Objects.equals(getAffiliationByUse(), aCustomerPackage.getAffiliationByUse())) { return false; }
		if (!Objects.equals(getHistoricUseColor(), aCustomerPackage.getHistoricUseColor())) { return false; }
		if (!Objects.equals(getHistoricUseDesign(), aCustomerPackage.getHistoricUseDesign())) { return false; }
		if (!Objects.equals(getStatusLifeCycle(), aCustomerPackage.getStatusLifeCycle())) { return false; }
		if (!Objects.equals(getStatusCataloging(), aCustomerPackage.getStatusCataloging())) { return false; }
		if (!Objects.equals(getStatusAutomation(), aCustomerPackage.getStatusAutomation())) { return false; }
		if (!Objects.equals(getStatusAvailability(), aCustomerPackage.getStatusAvailability())) { return false; }
		if (!Objects.equals(getBusinessDefaultUse(), aCustomerPackage.getBusinessDefaultUse())) { return false; }
		
		return true;

	}

	/**
	 * Return a CustomerPackageDocument representative of this CustomerPackage for addition to SOLR
	 */
	public CustomerPackageDocument getCustomerPackageDocument()
	{
		CustomerPackageDocument pd = new CustomerPackageDocument();
		pd.setDatabaseID(getID());
		pd.setName(getName());
		pd.setPackageID("PK_" + pd.getFormattedID(getPackageID()));
		pd.setPartID("PR_" + pd.getFormattedID(getaPackage().getPartID()));
		pd.setDesignID("DS_" + pd.getFormattedID(getaPackage().getDesignID()));
		pd.setClientID("CU_" + pd.getFormattedID(getCustomerID()));
		pd.setStatusLifeCycle(getStatusLifeCycle());
		pd.setStatusAvailability(getStatusAvailability());
		pd.setBusinessDefaultUse(getBusinessDefaultUse());
		
		return pd;
	}
	
	public AssetPackage getaPackage()
	{
		return aPackage;
	}
	public void setaPackage(AssetPackage aPackage)
	{
		this.aPackage = aPackage;
	}
}
