package com.jostens.jemm2.solr.web;

import org.junit.Test;

public class SOLRQueryTest
{


//	@Test
	public void test1Keyword()
	{
		SOLRQuery query = SOLRQuery.getActiveQuery();
		query.setQuery("Shark");
		query.performQuery();
		
		System.out.println("RESULTS = " + query.getResults());
	}

	@Test
	public void test1KeywordKeyEntry()
	{
		SOLRQuery query = SOLRQuery.getActiveQuery();
		query.setQuery("S");
		query.performQuery();		
		System.out.println("(S) RESULTS = " + query.getResults());
		query.setQuery("Sh");
		query.performQuery();		
		System.out.println("(Sh) RESULTS = " + query.getResults());
		query.setQuery("Sha");
		query.performQuery();		
		System.out.println("(Sha) RESULTS = " + query.getResults());
		query.setQuery("Shar");
		query.performQuery();		
		System.out.println("(Shar) RESULTS = " + query.getResults());
		query.setQuery("Shark");
		query.performQuery();		
		System.out.println("(Shark) RESULTS = " + query.getResults());
	}

}
