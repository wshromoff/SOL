package com.jostens.jemm2.solr.web;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import com.wassoftware.solr.ConnectToSolr;

/**
 * Manages document counts and changes in those counts for display in the web application
 */
public class DocumentCounts
{
	// This static instance holds the starting counts for the documents
	private static DocumentCounts startingCounts = null;
	
	private HttpSolrClient solr = null;
	
	private long designCount = 0;
	private long partCount = 0;

	public void generateCounts()
	{
		try
		{
			ConnectToSolr connect = new ConnectToSolr();
			solr = connect.makeConnection();
			
			designCount = getCountForContentType("Design");
			partCount = getCountForContentType("Part");

			solr.close();
			// If startingCounts is null then need to add this as a static instance for values at startup
			if (startingCounts == null)
			{
				startingCounts = this;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	private long getCountForContentType(String contentType) throws SolrServerException, IOException
	{
		SolrQuery query = new SolrQuery();
		query.set("q", "contentType:" + contentType);
		query.setRows(0);		// Data isn't returned only the counts
		QueryResponse response = solr.query(query);
		SolrDocumentList docList = response.getResults();
//		System.out.println("SIZE=" + docList.size());
		return docList.getNumFound();
	}

	public long getDesignCount()
	{
		return designCount;
	}
	public long getPartCount()
	{
		return partCount;
	}

	public long getDeltaDesignCount()
	{
		return designCount - startingCounts.getDesignCount();
	}
	public long getDeltaPartCount()
	{
		return partCount - startingCounts.getPartCount();
	}

	/**
	 * Return delta for all documents
	 */
	public long getDeltaCount()
	{
		long delta = getDeltaDesignCount() + getDeltaPartCount();
		return delta;
	}

	
}
