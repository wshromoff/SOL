package com.jostens.jemm2.pojo;

import java.util.Objects;

public class Asset
{
	private int ID = 0;
	private String name;
	private int packageID = 0;
	private	String folderPath;
	private int isBlack = 0;
	private int isGold = 0;
	private int isSilver = 0;
	private int isBestAvailable = 0;
	
	
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
	public String getFolderPath()
	{
		return folderPath;
	}
	public void setFolderPath(String folderPath)
	{
		this.folderPath = folderPath;
	}
	public int getIsBlack()
	{
		return isBlack;
	}
	public void setIsBlack(int isBlack)
	{
		this.isBlack = isBlack;
	}
	public int getIsGold()
	{
		return isGold;
	}
	public void setIsGold(int isGold)
	{
		this.isGold = isGold;
	}
	public int getIsSilver()
	{
		return isSilver;
	}
	public void setIsSilver(int isSilver)
	{
		this.isSilver = isSilver;
	}
	public int getIsBestAvailable()
	{
		return isBestAvailable;
	}
	public void setIsBestAvailable(int isBestAvailable)
	{
		this.isBestAvailable = isBestAvailable;
	}
	
	public boolean equals(Object obj)
	{
		Asset asset = (Asset)obj;

		if (!Objects.equals(getName(), asset.getName())) { return false; }
		if (!Objects.equals(getFolderPath(), asset.getFolderPath())) { return false; }

		if (getPackageID() != asset.getPackageID()) {return false;}
		if (getIsBlack() != asset.getIsBlack()) {return false;}
		if (getIsGold() != asset.getIsGold()) {return false;}
		if (getIsSilver() != asset.getIsSilver()) {return false;}
		if (getIsBestAvailable() != asset.getIsBestAvailable()) {return false;}
		
		return true;
	}
}
