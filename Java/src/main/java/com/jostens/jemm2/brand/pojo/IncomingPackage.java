package com.jostens.jemm2.brand.pojo;

/*
 * Object that represents an incoming package being processed by automations
 */
public class IncomingPackage
{
	private int ID = 0;
	private String name;
	private String statusAutomation;
	
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
	public String getStatusAutomation()
	{
		return statusAutomation;
	}
	public void setStatusAutomation(String statusAutomation)
	{
		this.statusAutomation = statusAutomation;
	}
	
	
}
