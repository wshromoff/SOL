package com.jostens.jemm2.solr.web;

import static org.junit.Assert.assertEquals;

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

//	@Test
	public void test1Package()
	{
		
		query.setQuery("BR006786_1680371_mascot_vector_flat_2t_dx_0x_svs_wts_yls_ors_bzs_bks_x_x_x_x.cdr");
		query.performQuery();
		
		System.out.println("Hit Count = " + query.getHitCount());
		assertEquals(1, query.getHitCount());
	}

//	@Test
	public void test1Part()
	{
		
		query.setQuery("CE015443");
		query.performQuery();
		
		System.out.println("Hit Count = " + query.getHitCount());
		assertEquals(1, query.getHitCount());
	}

//	@Test
	public void test1Part1Package()
	{
		
		query.setQuery("CE015443 BR006786_1680371_mascot_vector_flat_2t_dx_0x_svs_wts_yls_ors_bzs_bks_x_x_x_x.cdr");
		query.performQuery();
		
		System.out.println("Hit Count = " + query.getHitCount());
		assertEquals(2, query.getHitCount());
	}

//	@Test
	public void test1Keyword()
	{
		
		query.setQuery("Shark");
		query.performQuery();
		
		System.out.println("Hit Count = " + query.getHitCount());
	}
	
	@Test
	public void test2Keyword()
	{
		query.setQuery("Cat Ha");
		query.performQuery();
		
		System.out.println("RESULTS = " + query.getHitCount());
	}

//	@Test
	public void test1KeywordKeyEntry()
	{
		query.setQuery("S");
		query.performQuery();		
		System.out.println("(S) RESULTS = " + query.getHitCount());
		query.setQuery("Sh");
		query.performQuery();		
		System.out.println("(Sh) RESULTS = " + query.getHitCount());
		query.setQuery("Sha");
		query.performQuery();		
		System.out.println("(Sha) RESULTS = " + query.getHitCount());
		query.setQuery("Shar");
		query.performQuery();		
		System.out.println("(Shar) RESULTS = " + query.getHitCount());
		query.setQuery("Shark");
		query.performQuery();		
		System.out.println("(Shark) RESULTS = " + query.getHitCount());
	}

}
