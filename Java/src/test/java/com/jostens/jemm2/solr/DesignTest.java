package com.jostens.jemm2.solr;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.junit.BeforeClass;
import org.junit.Test;

import com.wassoftware.solr.ConnectToSolr;

public class DesignTest
{
	DocumentObjectBinder binder = new DocumentObjectBinder();

	@BeforeClass
	public static void abc() throws SolrServerException, IOException
	{

		Design design1 = new Design();
		design1.setDatabaseID(1);
		design1.addKeyword("Shark");
		design1.addKeyword("Blue");
		design1.setMultipleMainSubject(true);

		Design design2 = new Design();
		design2.setDatabaseID(2);
		design2.addKeyword("Car");
		design2.addKeyword("Blue");
		design2.setMainSubject("Yugo");

		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();

		solr.addBean(design1);
		solr.addBean(design2);
		solr.commit();
		solr.close();

	}

	@Test
	public void test() throws SolrServerException, IOException
	{
		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();

		SolrQuery query = new SolrQuery();
		query.set("q", "id:DS_000001");
		QueryResponse response = solr.query(query);
		
		SolrDocumentList docList = response.getResults();
		assertEquals(docList.getNumFound(), 1);
		Design design = binder.getBean(Design.class, docList.get(0));
		System.out.println("FOUND 1:\n" + design.toString());
		
		// Try query by matching keyword
		query.set("q", "keywords:blue");		// Case doesn't matter
		response = solr.query(query);

		System.out.println("     --- BLUE ----");
		docList = response.getResults();
		assertEquals(docList.getNumFound(), 2);
		design = binder.getBean(Design.class, docList.get(0));
		System.out.println("FOUND 1:\n" + design.toString());
		design = binder.getBean(Design.class, docList.get(1));
		System.out.println("FOUND 2:\n" + design.toString());

		solr.close();

	}

}
