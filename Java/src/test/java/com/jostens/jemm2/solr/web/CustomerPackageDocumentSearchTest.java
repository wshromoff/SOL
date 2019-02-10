package com.jostens.jemm2.solr.web;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class CustomerPackageDocumentSearchTest
{
	private static SOLRQuery query = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		// Set this is a part document search
		query = SOLRQuery.getActiveQuery();
		query.setDocument("customerPackage");
		query.setHitCountOnly(true);
	}

//	@Test
	public void test1CustomerPackage()
	{
		
		query.setQuery("BR006786_1680371_mascot_vector_flat_2t_dx_0x_svs_wts_yls_ors_bzs_bks_x_x_x_x");
		query.performQuery();
		
		System.out.println("Hit Count = " + query.getHitCount());
//		System.out.println("Results = " + query.getResultIDs());
//		assertEquals(1, query.getHitCount());
	}

//	@Test
	public void test1Package()
	{
		
		query.setQuery("BR006786_mascot_vector_flat_2t_dx_0x_svs_wts_yls_ors_bzs_bks_x_x_x_x");
		query.performQuery();
		
		System.out.println("Hit Count = " + query.getHitCount());
//		System.out.println("Results = " + query.getResultIDs());
//		assertEquals(1, query.getHitCount());
	}

//	@Test
	public void test1Part()
	{
		
		query.setQuery("BR019501");
		query.performQuery();
		
		System.out.println("Hit Count = " + query.getHitCount());
		assertEquals(1, query.getHitCount());
	}

//	@Test
	public void test1Customer()
	{
		
		query.setQuery("1680371");
		query.performQuery();
		
		System.out.println("Hit Count = " + query.getHitCount());
		assertEquals(1, query.getHitCount());
	}

	@Test
	public void test1Keyword()
	{
		
		query.setQuery("Shark");
		query.performQuery();
		
		System.out.println("Hit Count = " + query.getHitCount());
	}

}
