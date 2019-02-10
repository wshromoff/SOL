package com.jostens.jemm2.solr.web;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;

/**
 * Perform Customer specific search.
 * 
 * Customers IDs are ignored as part of this search
 */
public class CustomerDocumentSearch extends QueryBase
{

	@Override
	protected void performDocumentQuery() throws SolrServerException, IOException
	{
		if (!customers.isEmpty())
		{
			// Perform customerID query
			String customerID = customers.get(0);
			StringBuffer sb = new StringBuffer(customerID);
			for (int i = 1; i < customers.size(); i++)
			{
				customerID = customers.get(i);
				sb.append(" OR " + customerID);
			}
			String queryText = "customerID:(" + sb.toString() + ")";
			System.out.println("QT[2-1] " + queryText);
			SolrQuery query = new SolrQuery();
			query.set("q", queryText);
			
			executeQuery(query);
		}

	}

}
