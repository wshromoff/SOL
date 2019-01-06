package com.wassoftware.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;

public class ConnectToSolr
{

	public HttpSolrClient makeConnection() throws SolrServerException, IOException
	{
		String urlString = "http://localhost:8983/solr/bigboxstore";
		HttpSolrClient solr = new HttpSolrClient.Builder(urlString).build();
		solr.setParser(new XMLResponseParser());
		
		return solr;
		
	}
	
	public void closeConnection(HttpSolrClient solr) throws IOException
	{
		solr.close();
	}
}
