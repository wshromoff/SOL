package com.jostens.jemm2.solr.web;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.jostens.jemm2.jdbc.ConnectionHelper;
import com.jostens.jemm2.jdbc.Jemm2Statements;
import com.wassoftware.solr.ConnectToSolr;

/**
 * Base abstract class for the different forms of SOLR queries.  Which implementation class
 * is used is dependent on a radio button selection on the form that determines the document
 * type being searched.
 */
public abstract class QueryBase
{
	private ConnectToSolr connect = null;
	private HttpSolrClient solr = null;

	protected List<String> tokens = new ArrayList<String>();	// The query broken into tokens
	
	// Tokens are organized into one of these buckets that an implementation query class
	// can easily use to find matches
	protected List<String> keywords = new ArrayList<String>();
	protected List<String> parts = new ArrayList<String>();
	protected List<String> customers = new ArrayList<String>();
	protected List<String> packages = new ArrayList<String>();

	private List<String> resultIDs = new ArrayList<String>();		// If ID's are desired
	private long hitCount = 0;		// The count of hits found

	// Form attributes that are part of the query
	protected String query = "";		// Current user query

	// Flag to indicate if hit count only should only be calculated
	private boolean hitCountOnly = true;
	
	// Make a database connection available
	protected Connection c = null;


	/**
	 * Entry point for a SOLR query.  This method will perform initialization and then call the
	 * document specific implementation class.
	 */
	public void performQuery()
	{
		resultIDs.clear();
		keywords.clear();
		parts.clear();
		customers.clear();
		packages.clear();
		hitCount = 0;
		
		if (tokens.isEmpty())
		{
			// There are no tokens - no query to perform
			return;
		}

		Jemm2Statements statements = new Jemm2Statements();
		statements.initializeStatements();
		c = ConnectionHelper.getJEMM2Connection();

		try
		{
			connect = new ConnectToSolr();
			solr = connect.makeConnection();
			
			// Examine tokens and place in appropriate word buckets
			examineTokens();
			
			// Perform the document specific queries
			performDocumentQuery();
	
			connect.closeConnection(solr);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		ConnectionHelper.closeConnection(c);

	}
	
	/*
	 * Look at tokens and determine which word bucket is most appropriate for this token
	 */
	private void examineTokens() throws SolrServerException, IOException
	{
		for (String token : tokens)
		{
			if (isPackageToken(token))
			{
				packages.add(token);
				continue;
			}
			if (isCustomerToken(token))
			{
				customers.add(token);
				continue;
			}
			if (isPartToken(token))
			{
				parts.add(token);
				continue;
			}
			// Final option has token placed into keywords List
			keywords.add(token);
		}
	}
	
	private boolean isPackageToken(String token)
	{
		// To be a package name it must include a '_'
		if (token.contains("_"))
		{
			return true;
		}
		
		return false;
	}
	private boolean isCustomerToken(String token)
	{
		// A customer ID must be 7 characters & numeric
		if (token.length() != 7)
		{
			return false;
		}
		String regex = "\\d+";
		if (!token.matches(regex))
		{
			return false;		// Not all numeric
		}
		
		return true;
	}

	private boolean isPartToken(String token) throws SolrServerException, IOException
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

	/**
	 * Abstract method that extender document search classes must implement
	 */
	protected abstract void performDocumentQuery()  throws Exception;

	public String getQuery()
	{
		return query;
	}
	public void setQuery(String query)
	{
		this.query = query;
		// Break the query into queries separated by space
		tokens.clear();
		StringTokenizer st = new StringTokenizer(query, " ");
		while (st.hasMoreTokens())
		{
			String token = st.nextToken().trim();
			tokens.add(token);
		}
	}
	public void setHitCountOnly(boolean hitCountOnly)
	{
		this.hitCountOnly = hitCountOnly;
	}
	
	/**
	 * Perform the supplied query updating row count found or IDs if asked for
	 * @throws IOException 
	 * @throws SolrServerException 
	 */
	public void executeQuery(SolrQuery query) throws SolrServerException, IOException
	{
		if (hitCountOnly)
		{
			query.setRows(0);
		}
		else
		{
			query.setFields("id");
			query.setRows(15);
		}
		QueryResponse response = solr.query(query);
		
		SolrDocumentList docList = response.getResults();
		if (hitCountOnly)
		{
			hitCount = hitCount + docList.getNumFound();
		}
		else
		{
			for (SolrDocument doc : docList)
			{
				if (resultIDs.size() >= 15)
				{
					break;
				}
				String documentID = (String)doc.getFieldValue("id");
				resultIDs.add(documentID);
				hitCount++;
			}
		}
	}

	public long getHitCount()
	{
		return hitCount;
	}

	public List<String> getResultIDs()
	{
		return resultIDs;
	}
	
}
