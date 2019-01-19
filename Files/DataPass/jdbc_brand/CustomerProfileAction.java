package com.jostens.dam.brand.jdbc;

/**
 * Represents a row in the MB_ACTIONS table.  This object will be passed around to
 * perform updates to the MB_ACTIONS table.
 */
public class CustomerProfileAction
{
	// These statics are the defined Actions
	public static String ADD_ACTION = "Add";
	public static String DELETE_ACTION = "Delete";
	public static String METADATA_ACTION = "Metadata";
	
	private String ID = null;
	private String action = null;
	private String assetName = null;
	private String mbGUID = null;
	private String metadata = null;
	private String message = null;
	private String customerID = null;

	public boolean isAddAction()
	{
		if (action != null && ADD_ACTION.equals(action))
		{
			return true;
		}
		return false;
	}
	public boolean isDeleteAction()
	{
		if (action != null && DELETE_ACTION.equals(action))
		{
			return true;
		}
		return false;
	}
	public boolean isMetadataAction()
	{
		if (action != null && METADATA_ACTION.equals(action))
		{
			return true;
		}
		return false;
	}
	public String getID()
	{
		return ID;
	}
	public void setID(String iD)
	{
		ID = iD;
	}
	public String getAction()
	{
		return action;
	}
	public void setAction(String action)
	{
		this.action = action;
	}
	public String getAssetName()
	{
		return assetName;
	}
	public void setAssetName(String assetName)
	{
		this.assetName = assetName;
	}
	public String getMbGUID()
	{
		return mbGUID;
	}
	public void setMbGUID(String mbGUID)
	{
		this.mbGUID = mbGUID;
	}
	public String getMetadata()
	{
		return metadata;
	}
	public void setMetadata(String metadata)
	{
		this.metadata = metadata;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getCustomerID()
	{
		return customerID;
	}
	public void setCustomerID(String customerID)
	{
		this.customerID = customerID;
	}
}
