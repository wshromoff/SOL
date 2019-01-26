package com.jostens.jemm2.solr;

import static org.junit.Assert.assertEquals;

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
import com.jostens.jemm2.jdbc.helpers.PartDatabaseHelper;
import com.jostens.jemm2.pojo.Part;
import com.wassoftware.solr.ConnectToSolr;

public class PartDocumentTest
{
	DocumentObjectBinder binder = new DocumentObjectBinder();
	private static Connection c = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
//		PartDocument part1 = new PartDocument();
//		part1.setDatabaseID(1);
//		part1.setName("JR12345");
//		part1.setDesignID("DS_000001");

//		ConnectToSolr connect = new ConnectToSolr();
//		HttpSolrClient solr = connect.makeConnection();
//
//		solr.addBean(part1);
//		solr.commit();
//		solr.close();
		
		Jemm2Statements statements = new Jemm2Statements();
		statements.initializeStatements();
		c = ConnectionHelper.getJEMM2Connection();

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		ConnectionHelper.closeConnection(c);
	}

	@Test
	public void test() throws SolrServerException, IOException
	{
		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();

		SolrQuery query = new SolrQuery();
		query.set("q", "{!join from=id to=designID}keywords:Shark");
		QueryResponse response = solr.query(query);
		
		SolrDocumentList docList = response.getResults();
		assertEquals(docList.getNumFound(), 1);
		PartDocument part = binder.getBean(PartDocument.class, docList.get(0));
		System.out.println("FOUND 1:\n" + part.toString());

		solr.close();

	}

	// Now test grabbing Part 1 from Oracle and add to SOLR
//	@Test
	public void testAddPart() throws SolrServerException, IOException, SQLException
	{

		PartDatabaseHelper helper = new PartDatabaseHelper();
		
		Part part = new Part();
		part.setID(7);
		
		helper.getPart(c, part);
		
		System.out.println("Name: " + part.getName());

		PartDocument pd = part.getPartDocument();
		System.out.println("Design ID: " + pd.getDesignID());
		
		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();

		solr.addBean(pd);
		solr.commit();
		solr.close();

	}

}
