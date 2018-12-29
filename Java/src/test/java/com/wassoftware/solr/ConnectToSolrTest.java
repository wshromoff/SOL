package com.wassoftware.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;

import junit.framework.TestCase;

public class ConnectToSolrTest extends TestCase
{

	@Test
	public void testConnection() throws SolrServerException, IOException
	{
		System.out.println("Starting test.");
		ConnectToSolr connect = new ConnectToSolr();
		connect.makeConnection();
	}
}
