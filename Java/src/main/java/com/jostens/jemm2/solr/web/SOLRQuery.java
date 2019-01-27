package com.jostens.jemm2.solr.web;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import com.wassoftware.solr.ConnectToSolr;

/**
 * Object that performs a SOLR query and is called from the web application
 */
public class SOLRQuery
{
	// This static instance holds the users current query
	private static SOLRQuery activeQuery = null;
	
	private String query = "";		// Current user query
	
	private long results = 0;		// Documents found

	public static SOLRQuery getActiveQuery()
	{
		if (activeQuery == null)
		{
			activeQuery = new SOLRQuery();
		}
		return activeQuery;
	}
	
	public static void newQuery()
	{
		// Reset to a new query
		activeQuery = new SOLRQuery();		
	}

	public String getQuery()
	{
		return query;
	}
	public void setQuery(String query)
	{
		this.query = query;
	}
	public long getResults()
	{
		return results;
	}

	public void performQuery()
	{
		if (query == null || query.trim().length() == 0)
		{
			// Nothing set so query can't be completed
			results = 0;
			return;
		}
		
		try
		{
			ConnectToSolr connect = new ConnectToSolr();
			HttpSolrClient solr = connect.makeConnection();
			
			SolrQuery query = new SolrQuery();
			query.set("q", "{!join from=id to=designID}keywords:" + getQuery() + "*");
			query.setRows(0);
			QueryResponse response = solr.query(query);
			
			SolrDocumentList docList = response.getResults();
			results = docList.getNumFound();

		} catch (Exception e)
		{
			e.printStackTrace();
			results = 0;
		}


	}
}
