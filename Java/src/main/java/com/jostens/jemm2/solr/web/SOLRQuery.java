package com.jostens.jemm2.solr.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
	private ConnectToSolr connect = null;
	private HttpSolrClient solr = null;

	// This static instance holds the users current query
	private static SOLRQuery activeQuery = null;
	
	private String query = "";		// Current user query
	
	private List<String> keywords = new ArrayList<String>();
	private List<String> parts = new ArrayList<String>();
	
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
		
		// Clear the parsed strings
		keywords.clear();
		parts.clear();
		results = 0;
		
		try
		{
			connect = new ConnectToSolr();
			solr = connect.makeConnection();

			// Tokenize the query and determine if Part or valid keyword
			StringTokenizer st = new StringTokenizer(query, " ");
			while (st.hasMoreTokens())
			{
				String token = st.nextToken();
				if (tokenIsPart(token))
				{
					System.out.println("Part: " + token);
					parts.add(token);
					continue;
				}
				keywords.add("keywords:" + token + "*");
			}
			
			if (keywords.isEmpty() && parts.isEmpty())
			{
				// No matches
				results = 0;
				return;
			}

			if (!keywords.isEmpty())
			{
				StringBuffer sb = new StringBuffer(keywords.get(0));
				for (int i = 1; i < keywords.size(); i++)
				{
					sb.append(" AND " + keywords.get(i));
				}
				System.out.println("--->" + sb.toString());
	
				SolrQuery query = new SolrQuery();
	//			query.set("q", "{!join from=id to=designID}keywords:" + getQuery() + "*");
				query.set("q", "{!join from=id to=designID}(" + sb.toString() + ")");
				query.setRows(0);
				QueryResponse response = solr.query(query);
				
				SolrDocumentList docList = response.getResults();
				results = results + docList.getNumFound();
			}
			
			if (!parts.isEmpty())
			{
				StringBuffer sb = new StringBuffer(parts.get(0));
				for (int i = 1; i < parts.size(); i++)
				{
					sb.append(" OR " + parts.get(i));
				}
				System.out.println("--->" + sb.toString());
	
				SolrQuery query = new SolrQuery();
	//			query.set("q", "{!join from=id to=designID}keywords:" + getQuery() + "*");
				query.set("q", "name:(" + sb.toString() + ")");
				query.setRows(0);
				QueryResponse response = solr.query(query);
				
				SolrDocumentList docList = response.getResults();
				results = results + docList.getNumFound();
				
			}

		} catch (Exception e)
		{
			e.printStackTrace();
			results = 0;
		}
	}
	
	private boolean tokenIsPart(String token) throws SolrServerException, IOException
	{
		SolrQuery query = new SolrQuery();
		query.set("q", "contentType:Part");
		query.setRows(0);
		query.setFilterQueries("name:" + token);
		QueryResponse response = solr.query(query);
		
		SolrDocumentList docList = response.getResults();
		long results = docList.getNumFound();
		if (results > 0)
		{
			return true;
		}
		return false;
	}
}
