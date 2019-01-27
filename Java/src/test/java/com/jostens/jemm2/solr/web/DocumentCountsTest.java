package com.jostens.jemm2.solr.web;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.jemm2.solr.DesignDocument;
import com.wassoftware.solr.ConnectToSolr;

public class DocumentCountsTest
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

//	@Test
	public void testGetDocumentCounts()
	{
		DocumentCounts documentCounts = new DocumentCounts();
		documentCounts.generateCounts();
		
		System.out.println("Design Counts = " + documentCounts.getDesignCount());
		System.out.println("Part Counts = " + documentCounts.getPartCount());
	}

	@Test
	public void testGetDeltaCounts() throws SolrServerException, IOException
	{
		DocumentCounts documentCounts = new DocumentCounts();
		documentCounts.generateCounts();

		System.out.println("Design Counts = " + documentCounts.getDesignCount());
		System.out.println("Part Counts = " + documentCounts.getPartCount());

		// Add a temporary Design to show counts change
		DesignDocument design2 = new DesignDocument();
		design2.setDatabaseID(9999);
		design2.setMainSubject("Yugo");

		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();

		solr.addBean(design2);
		solr.commit();

		documentCounts = new DocumentCounts();
		documentCounts.generateCounts();
		
		System.out.println("Delta Design Counts = " + documentCounts.getDeltaDesignCount());
		System.out.println("Delta Part Counts = " + documentCounts.getDeltaPartCount());
		System.out.println("All Delta Counts = " + documentCounts.getDeltaCount());

		// Have the following uncommented to delay deleting the added design to observe what the web project might do
		try
		{
			Thread.sleep(90000);
		} catch (InterruptedException e)
		{}
		
		solr.deleteByQuery("databaseID:9999");
		solr.commit();
		solr.close();

	}

}
