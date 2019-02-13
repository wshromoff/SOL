package com.jostens.jemm2.brand.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Object that represents an incoming package being processed by automations
 */
public class IncomingPackage
{
	private int ID = 0;
	private String name;
	private String statusAutomation;
	private String error;
	private List<IncomingAsset> assets = new ArrayList<IncomingAsset>();
	private Map<String, String> metadata = new HashMap<String, String>();
	
	public int getID()
	{
		return ID;
	}
	public String getIDasString()
	{
		return ID + "";
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
	public String getStatusAutomation()
	{
		return statusAutomation;
	}
	public void setStatusAutomation(String statusAutomation)
	{
		this.statusAutomation = statusAutomation;
	}

	public String getError()
	{
		return error;
	}
	public void setError(String error)
	{
		this.error = error;
	}
	public List<IncomingAsset> getAssets()
	{
		return assets;
	}
	public void setAssets(List<IncomingAsset> assets)
	{
		this.assets = assets;
	}
	public void addIncomingAsset(IncomingAsset asset)
	{
		assets.add(asset);
	}
	public boolean addIncomingName(String pngName)
	{
		// If the .png has the same name as this package add it as an incoming asset
		int i = getName().indexOf(".");
		String subName = getName().substring(0, i);
		if (pngName.startsWith(subName))
		{
			// Found a match
			IncomingAsset asset = new IncomingAsset();
			asset.setName(pngName);
			addIncomingAsset(asset);
			return true;
		}
		return false;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getName() + "\n");
		for (IncomingAsset asset : assets)
		{
			sb.append("   " + asset.getName() + "\n");			
		}
		return sb.toString();
	}
	public Map<String, String> getMetadata()
	{
		return metadata;
	}
	public void setMetadata(Map<String, String> metadata)
	{
		this.metadata = metadata;
	}
	public void addMetadata(String key, String value)
	{
		getMetadata().put(key, value);
	}
	
}
