package com.jostens.jemm2.solr;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.jostens.jemm2.jdbc.helpers.DesignDatabaseHelper;
import com.jostens.jemm2.pojo.Design;
import com.wassoftware.solr.ConnectToSolr;

public class DesignDocumentTest
{
	DocumentObjectBinder binder = new DocumentObjectBinder();
	private static Connection c = null;

	@BeforeClass
	public static void abc() throws SolrServerException, IOException
	{

		DesignDocument design1 = new DesignDocument();
		design1.setDatabaseID(1);
		design1.setName("Design1");
		design1.addKeyword("Shark");
		design1.addKeyword("Blue");
		design1.setAffiliationByDepiction("Depiction 1");
		design1.setBrandAssetType("type 1");
		design1.setMascotName("mascot 1");

		DesignDocument design2 = new DesignDocument();
		design2.setDatabaseID(2);
		design2.setName("Design2");
		design2.addKeyword("Car");
		design2.addKeyword("Blue");
		design2.setMainSubject("Yugo");

		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();

		solr.addBean(design1);
		solr.addBean(design2);
		solr.commit();
		solr.close();
		
		Jemm2Statements statements = new Jemm2Statements();
		statements.initializeStatements();
		c = ConnectionHelper.getJEMM2Connection();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		ConnectionHelper.closeConnection(c);
	}

//	@Test
	public void test() throws SolrServerException, IOException
	{
		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();

		SolrQuery query = new SolrQuery();
		query.set("q", "id:DS_000001");
		QueryResponse response = solr.query(query);
		
		SolrDocumentList docList = response.getResults();
		assertEquals(docList.getNumFound(), 1);
		DesignDocument design = binder.getBean(DesignDocument.class, docList.get(0));
		System.out.println("FOUND 1:\n" + design.toString());
		
		// Try query by matching keyword
		query.set("q", "keywords:blue");		// Case doesn't matter
		response = solr.query(query);

		System.out.println("     --- BLUE ----");
		docList = response.getResults();
		assertEquals(docList.getNumFound(), 2);
		design = binder.getBean(DesignDocument.class, docList.get(0));
		System.out.println("FOUND 1:\n" + design.toString());
		design = binder.getBean(DesignDocument.class, docList.get(1));
		System.out.println("FOUND 2:\n" + design.toString());

		solr.close();

	}

	// Now test grabbing design 1 and add to SOLR
	@Test
	public void testAddDesign() throws SolrServerException, IOException, SQLException
	{

		DesignDatabaseHelper helper = new DesignDatabaseHelper();
		
		Design design = new Design();
		design.setID(7);
		
		helper.getDesign(c, design);
		
		System.out.println("Name: " + design.getName());
		System.out.println("Keywords: " + design.getKeywords().toString());

		DesignDocument dd = design.getDesignDocument();
		
		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();

		solr.addBean(dd);
		solr.commit();
		solr.close();

	}
}
