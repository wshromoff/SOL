package com.jostens.jemm2.solr.web;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class CustomerDocumentSearchTest
{
	private static SOLRQuery query = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		// Set this is a part document search
		query = SOLRQuery.getActiveQuery();
		query.setDocument("customer");
		query.setHitCountOnly(false);
	}

	@Test
	public void test1CustomerID()
	{
		
		query.setQuery("1045877");
		query.performQuery();
		
		System.out.println("Hit Count = " + query.getHitCount());
		System.out.println("Documents = " + query.getResultIDs());
		assertEquals(1, query.getHitCount());
	}

}
