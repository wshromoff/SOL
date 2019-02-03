package com.wassoftware.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.junit.Test;

public class SOLRAdmin
{

//	@Test
	public void deleteAllSOLRData() throws SolrServerException, IOException
	{
		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();
		solr.deleteByQuery("*:*");
		solr.commit();
		solr.close();

	}

//	@Test
	public void deleteDesignSOLRData() throws SolrServerException, IOException
	{
		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();
		solr.deleteByQuery("contentType:Design");
		solr.commit();
		solr.close();

	}

//	@Test
	public void deletePartSOLRData() throws SolrServerException, IOException
	{
		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();
		solr.deleteByQuery("contentType:Part");
		solr.commit();
		solr.close();

	}

//	@Test
	public void deleteCustomerSOLRData() throws SolrServerException, IOException
	{
		ConnectToSolr connect = new ConnectToSolr();
		HttpSolrClient solr = connect.makeConnection();
		solr.deleteByQuery("contentType:Customer");
		solr.commit();
		solr.close();

	}

}
