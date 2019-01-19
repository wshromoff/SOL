package com.jostens.jemm2.helpers;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.junit.BeforeClass;
import org.junit.Test;

import com.wassoftware.solr.ConnectToSolr;

public class SummaryHelperTest
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	@Test
	public void testCountDesigns() throws Exception
	{
		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();
		
		SummaryHelper helper = new SummaryHelper();
		helper.setClient(solr);
		
		helper.countDesigns();
		
		System.out.println("Design Count = " + helper.getDesignCount());
		
		connect.closeConnection(solr);
	}

	@Test
	public void testCountParts() throws Exception
	{
		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();
		
		SummaryHelper helper = new SummaryHelper();
		helper.setClient(solr);
		
		helper.countParts();
		
		System.out.println("Part Count = " + helper.getPartCount());
		
		connect.closeConnection(solr);
	}

}
