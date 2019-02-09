package com.jostens.jemm2.solr.web;

import org.junit.Test;

public class SOLRQueryTest
{


	@Test
	public void test1Keyword()
	{
		SOLRQuery query = SOLRQuery.getActiveQuery();
		query.setQuery("Shark");
		query.performQuery(true);
		
		System.out.println("RESULTS = " + query.getHitCount());
	}

//	@Test
	public void test1KeywordKeyEntry()
	{
		SOLRQuery query = SOLRQuery.getActiveQuery();
		query.setQuery("S");
		query.performQuery(true);		
		System.out.println("(S) RESULTS = " + query.getHitCount());
		query.setQuery("Sh");
		query.performQuery(true);		
		System.out.println("(Sh) RESULTS = " + query.getHitCount());
		query.setQuery("Sha");
		query.performQuery(true);		
		System.out.println("(Sha) RESULTS = " + query.getHitCount());
		query.setQuery("Shar");
		query.performQuery(true);		
		System.out.println("(Shar) RESULTS = " + query.getHitCount());
		query.setQuery("Shark");
		query.performQuery(true);		
		System.out.println("(Shark) RESULTS = " + query.getHitCount());
	}

	// Test 2 keywords anded together
//	@Test
	public void test2Keyword()
	{
		SOLRQuery query = SOLRQuery.getActiveQuery();
		query.setQuery("Cat Ha");
		query.performQuery(true);
		
		System.out.println("RESULTS = " + query.getHitCount());
	}

	// Test 2 parts anded together
//	@Test
	public void testPartsCount()
	{
		SOLRQuery query = SOLRQuery.getActiveQuery();
		query.setQuery("BR000795 CE015443");
		query.performQuery(true);
		
		System.out.println("Part count found = " + query.getHitCount());
	}
	// Test 2 parts anded together showing IDs
//	@Test
	public void testPartsIDs()
	{
		SOLRQuery query = SOLRQuery.getActiveQuery();
		query.setQuery("BR000795 CE015443");
		query.performQuery(false);
		
		System.out.println("Part IDs found = " + query.getResultIDs());
	}
	// Test 2 parts for keywords
//	@Test
	public void testKeywordIDs()
	{
		SOLRQuery query = SOLRQuery.getActiveQuery();
		query.setQuery("Shark");
		query.doPartSearch();
		query.performQuery(false);
		
		System.out.println("Part IDs found = " + query.getResultIDs());
	}

	// Test Customer Searches
//	@Test
	public void testCustomer()
	{
		SOLRQuery query = SOLRQuery.getActiveQuery();
		query.setQuery("1026953");
		query.doCustomerSearch();
		query.performQuery(false);
		
		System.out.println("Customer IDs found = " + query.getResultIDs());
	}

}
