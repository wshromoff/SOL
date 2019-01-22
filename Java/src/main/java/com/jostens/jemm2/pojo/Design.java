package com.jostens.jemm2.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Design
{
	private int ID = 0;
	private String name;
	private List<String> keywords = new ArrayList<String>();

	// 20180817-16357a3a71ce|School|20180817-16357a3a71ce|20180817-16357a3a71ce|Mascot|Custom (Not Stock)|||||||Common (For Many)|Manufacturing|Titan,Mythology,Spartan,Trojan|Titan|false|Head|Side View|
	public Design()
	{
		
	}
	public Design(String exportString)
	{
		String[] stringArr = exportString.split("\\|");
		setName(stringArr[0]);
		setKeywords(stringArr[14]);
		
//		System.out.println("-->" + getName());

//		StringTokenizer st = new StringTokenizer(exportString, "|");
//		setName(st.nextToken());
//		System.out.println("-->" + getName());
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		setKeywords(st.nextToken());
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
	
	public boolean equals(Object obj)
	{
		Design design = (Design)obj;

		if (getID() != design.getID())
		{
			return false;
		}
		if (!getName().equals(design.getName()))
		{
			return false;
		}
		if (!getKeywords().equals(design.getKeywords()))
		{
			return false;
		}
		return true;

	}
}
