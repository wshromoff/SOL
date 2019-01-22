package com.jostens.jemm2.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Design
{
	private int ID = 0;
	private String name;
	private List<String> keywords = new ArrayList<String>();
	
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
	public List<String> getKeywords()
	{
		return keywords;
	}
	public void setKeywords(List<String> keywords)
	{
		this.keywords = keywords;
	}
	public void setKeywords(String keywordsAsString)
	{
		// Keywords passed as comma separate String - Break into String array
		keywords.clear();
		StringTokenizer st = new StringTokenizer(keywordsAsString, ",");
		while (st.hasMoreTokens())
		{
			String keyword = st.nextToken();
			keywords.add(keyword);
		}
	}
	
	public void addKeyword(String keyword)
	{
		keywords.add(keyword);
	}
}
