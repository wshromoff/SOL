package com.jostens.jemm2.solr.web;

import org.junit.BeforeClass;
import org.junit.Test;

public class PartDocumentSearchTest
{
	private static SOLRQuery query = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		// Set this is a part document search
		query = SOLRQuery.getActiveQuery();
		query.setDocument("part");
		query.setHitCountOnly(true);
	}

	@Test
	public void test1Keyword()
	{
		
		query.setQuery("Shark");
		query.performQuery();
		
		System.out.println("Hit Count = " + query.getHitCount());
	}

}
