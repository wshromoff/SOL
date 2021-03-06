package com.jostens.jemm2.solr.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.wassoftware.solr.ConnectToSolr;

/**
 * Object that performs a SOLR query and is called from the SearchServlet
 */
public class SOLRQuery
{
	private ConnectToSolr connect = null;
	private HttpSolrClient solr = null;

	// This static instance holds the users current query (Instantaion of this object)
	private static SOLRQuery activeQuery = null;

	// This is the search implementation being called
	private QueryBase documentQuery = new PartDocumentSearch();
	
//	private String query = "";		// Current user query
	private boolean partSearch = true;		// Searching for parts
	private boolean customerSearch = false;		// Searching for customers
	private boolean packageSearch = false;		// Searching for packages
	private boolean customerPackageSearch = false;		// Searching for customer packages

	
	private List<String> keywords = new ArrayList<String>();
	private List<String> parts = new ArrayList<String>();
	private List<String> customers = new ArrayList<String>();

	private List<String> resultIDs = new ArrayList<String>();	
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

	public void performQuery()
	{
		// Call the perform query method on the document search implementation class
		documentQuery.performQuery();
	}
	/**
	 * Count Only - Set parameter to true if count only is desired
	 */
	public void performQuery(boolean countOnly)
	{
		if (getQuery() == null || getQuery().trim().length() == 0)
		{
			// Nothing set so query can't be completed
			results = 0;
			return;
		}
		
		// Clear the parsed strings
		keywords.clear();
		parts.clear();
		customers.clear();
		resultIDs.clear();
		results = 0;
		
		try
		{
			connect = new ConnectToSolr();
			solr = connect.makeConnection();

			// Break apart the query by term and determine what it should be part of searching
			tokenizeQuery(getQuery());
			
			if (keywords.isEmpty() && parts.isEmpty() && customers.isEmpty())
			{
				// No matches
				results = 0;
				return;
			}
			
			if (!customers.isEmpty())
			{
				// Searching for customer information
			}

			if (!keywords.isEmpty())
			{
				StringBuffer sb = new StringBuffer(keywords.get(0));
				for (int i = 1; i < keywords.size(); i++)
				{
					sb.append(" AND " + keywords.get(i));
				}
				System.out.println("Keywords: --->" + sb.toString());
	
				SolrQuery query = new SolrQuery();
	//			query.set("q", "{!join from=id to=designID}keywords:" + getQuery() + "*");
				query.set("q", "{!join from=id to=designID}(" + sb.toString() + ")");
				query.setRows(0);
				if (countOnly)
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
				if (countOnly)
				{
					results = results + docList.getNumFound();
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
					}
				}
			}
			
			if (!parts.isEmpty())
			{
				StringBuffer sb = new StringBuffer(parts.get(0));
				for (int i = 1; i < parts.size(); i++)
				{
					sb.append(" OR " + parts.get(i));
				}
				System.out.println("Parts: --->" + sb.toString());
	
				SolrQuery query = new SolrQuery();
	//			query.set("q", "{!join from=id to=designID}keywords:" + getQuery() + "*");
				query.set("q", "name:(" + sb.toString() + ")");
				if (countOnly)
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
				if (countOnly)
				{
					results = results + docList.getNumFound();
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
					}
				}
				
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

	/*
	 * Look at each term in the query along with document type selected to determine if it's
	 * a keyword search, part search or customer search term.
	 */
	private void tokenizeQuery(String query) throws SolrServerException, IOException
	{
		// Tokenize the query and determine if Part or valid keyword
		StringTokenizer st = new StringTokenizer(query, " ");
		while (st.hasMoreTokens())
		{
			String token = st.nextToken();
			if (isCustomerSearch())
			{
				System.out.println("Customer: " + token + "*");
				customers.add(token);
				continue;
			}
			if (tokenIsPart(token))
			{
				System.out.println("Part: " + token);
				parts.add(token);
				continue;
			}
			keywords.add("keywords:" + token + "*");
		}
		
	}

	
	public void doPartSearch()
	{
		setDocument("part");
	}
	public void doCustomerSearch()
	{
		setDocument("customer");
	}
	public void doPackageSearch()
	{
		setDocument("package");
	}

	// ----------------- Methods related to update document specific query objects --------------
	public void setDocument(String documentType)
	{
//		System.out.println("DOCUMENT = " + documentType);
		if ("part".equals(documentType))
		{
			// Did the document type change
			if (!partSearch)
			{
				documentQuery = new PartDocumentSearch();
			}
			partSearch = true;
			customerSearch = false;
			packageSearch = false;
			customerPackageSearch = false;
		}
		if ("customer".equals(documentType))
		{
			// Did the document type change
			if (!customerSearch)
			{
				documentQuery = new CustomerDocumentSearch();
			}
			partSearch = false;
			customerSearch = true;
			packageSearch = false;
			customerPackageSearch = false;
		}
		if ("package".equals(documentType))
		{
			// Did the document type change
			if (!packageSearch)
			{
				documentQuery = new PackageDocumentSearch();
			}
			partSearch = false;
			customerSearch = false;
			packageSearch = true;
			customerPackageSearch = false;
		}
		if ("customerPackage".equals(documentType))
		{
			// Did the document type change
			if (!customerPackageSearch)
			{
				documentQuery = new CustomerPackageDocumentSearch();
			}
			partSearch = false;
			customerSearch = false;
			packageSearch = false;
			customerPackageSearch = true;
		}
	}

	public void setHitCountOnly(boolean hitCountOnly)
	{
		documentQuery.setHitCountOnly(hitCountOnly);
	}
	public String getQuery()
	{
		return documentQuery.getQuery();
	}
	public void setQuery(String query)
	{
		documentQuery.setQuery(query);
	}
	public boolean isPartSearch()
	{
		return partSearch;
	}
	public boolean isCustomerSearch()
	{
		return customerSearch;
	}
	public boolean isPackageSearch()
	{
		return packageSearch;
	}
	public boolean isCustomerPackageSearch()
	{
		return customerPackageSearch;
	}
	public long getHitCount()
	{
		return documentQuery.getHitCount();
	}
	public List<String> getResultIDs()
	{
		return documentQuery.getResultIDs();
	}
	
}
