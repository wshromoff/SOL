package com.wassoftware.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;

public class ConnectToSolr
{

	public HttpSolrClient makeConnection() throws SolrServerException, IOException
	{
		System.out.println(" * 1 *");
		String urlString = "http://localhost:8983/solr/bigboxstore";
		System.out.println(" * 2 *");
		HttpSolrClient solr = new HttpSolrClient.Builder(urlString).build();
		System.out.println(" * 3 *" + solr);
		solr.setParser(new XMLResponseParser());
		System.out.println(" * 4 *");
		
		return solr;
		
//		SolrInputDocument document = new SolrInputDocument();
//		document.addField("id", "123456");
//		document.addField("name", "Kenmore Dishwasher");
//		document.addField("price", "599.99");
//		solr.add(document);
//		solr.commit();
	}
	
	public void closeConnection(HttpSolrClient solr) throws IOException
	{
		solr.close();
	}
}
