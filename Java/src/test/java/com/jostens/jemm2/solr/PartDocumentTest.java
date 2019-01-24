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

public class PartDocumentTest
{
	DocumentObjectBinder binder = new DocumentObjectBinder();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		PartDocument part1 = new PartDocument();
		part1.setDatabaseID(1);
		part1.setName("JR12345");
		part1.setDesignID("DS_000001");

		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();

		solr.addBean(part1);
		solr.commit();
		solr.close();
	}

	@Test
	public void test() throws SolrServerException, IOException
	{
		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();

		SolrQuery query = new SolrQuery();
		query.set("q", "{!join from=id to=designID}keywords:blue");
		QueryResponse response = solr.query(query);
		
		SolrDocumentList docList = response.getResults();
		assertEquals(docList.getNumFound(), 1);
		PartDocument part = binder.getBean(PartDocument.class, docList.get(0));
		System.out.println("FOUND 1:\n" + part.toString());

		solr.close();

	}

}
