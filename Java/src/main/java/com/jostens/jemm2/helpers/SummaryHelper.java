package com.jostens.jemm2.helpers;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import com.wassoftware.solr.ConnectToSolr;

public class SummaryHelper
{
	private HttpSolrClient client = null;
	
	private int designCount = 0;
	private int partCount = 0;

	// Count all the documents of various types
	public void countDocuments()
	{
		ConnectToSolr solr = new ConnectToSolr();
		try
		{
			client = solr.makeConnection();
			
			countDesigns();
			countParts();
			
			solr.closeConnection(client);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Count the design documents
	 */
	public void countDesigns() throws Exception
	{
		SolrQuery query = new SolrQuery();
		query.set("q", "contentType:Design");
		QueryResponse response = client.query(query);
		
		SolrDocumentList docList = response.getResults();
		designCount = (int)docList.getNumFound();
	}

	/**
	 * Count the part documents
	 */
	public void countParts() throws Exception
	{
		SolrQuery query = new SolrQuery();
		query.set("q", "contentType:Part");
		QueryResponse response = client.query(query);
		
		SolrDocumentList docList = response.getResults();
		partCount = (int)docList.getNumFound();
	}

	public int getDesignCount()
	{
		return designCount;
	}
	public void setDesignCount(int designCount)
	{
		this.designCount = designCount;
	}
	public int getPartCount()
	{
		return partCount;
	}
	public void setPartCount(int partCount)
	{
		this.partCount = partCount;
	}

	public void setClient(HttpSolrClient client)
	{
		this.client = client;
	}
	
}
