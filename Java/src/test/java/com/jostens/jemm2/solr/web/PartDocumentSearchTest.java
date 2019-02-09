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
	public void test1Package()
	{
		
		query.setQuery("BR006786_1680371_mascot_vector_flat_2t_dx_0x_svs_wts_yls_ors_bzs_bks_x_x_x_x.cdr");
		query.performQuery();
		
		System.out.println("Hit Count = " + query.getHitCount());
	}

//	@Test
	public void test1Keyword()
	{
		
		query.setQuery("Shark");
		query.performQuery();
		
		System.out.println("Hit Count = " + query.getHitCount());
	}

}
