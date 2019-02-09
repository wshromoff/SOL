package com.jostens.jemm2.solr.web;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;

/**
 * Perform Part specific search.
 * 
 * Customers IDs are ignored as part of this search
 */
public class PartDocumentSearch extends QueryBase
{

	@Override
	protected void performDocumentQuery() throws SolrServerException, IOException
	{
		if (!packages.isEmpty())
		{
			// Perform Part query off of first segment of name which is a part
			String partName = getPartIDFromPackageName(packages.get(0));
			StringBuffer sb = new StringBuffer(partName);
			for (int i = 1; i < packages.size(); i++)
			{
				partName = getPartIDFromPackageName(packages.get(i));
				sb.append(" OR " + partName);
			}
			String queryText = "name:(" + sb.toString() + ")";
			System.out.println("QT[1] " + queryText);
			SolrQuery query = new SolrQuery();
			query.set("q", queryText);
			
			executeQuery(query);
		}
	}

	private String getPartIDFromPackageName(String packageName)
	{
		int i = packageName.indexOf("_");
		return packageName.substring(0, i);
	}
}
