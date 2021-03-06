package com.jostens.jemm2.solr.web;

import org.apache.solr.client.solrj.SolrQuery;

/**
 * Perform Part specific search.
 * 
 * Customers IDs are ignored as part of this search
 */
public class PartDocumentSearch extends QueryBase
{

	@Override
	protected void performDocumentQuery() throws Exception
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
			System.out.println("QT[1-1] " + queryText);
			SolrQuery query = new SolrQuery();
			query.set("q", queryText);
			
			executeQuery(query);
		}
		if (!parts.isEmpty())
		{
			// Perform Part query off of first segment of name which is a part
			String partName = parts.get(0);
			StringBuffer sb = new StringBuffer(partName);
			for (int i = 1; i < parts.size(); i++)
			{
				partName = getPartIDFromPackageName(parts.get(i));
				sb.append(" OR " + partName);
			}
			String queryText = "name:(" + sb.toString() + ")";
			System.out.println("QT[1-2] " + queryText);
			SolrQuery query = new SolrQuery();
			query.set("q", queryText);
			
			executeQuery(query);
		}
		
		if (!keywords.isEmpty())
		{
			StringBuffer sb = new StringBuffer(keywords.get(0) + "*");
			for (int i = 1; i < keywords.size(); i++)
			{
				sb.append(" AND " + keywords.get(i) + "*");
			}

			String queryText = "{!join from=id to=designID}keywords:(" + sb.toString() + ")";
			System.out.println("QT[1-3] " + queryText);

			SolrQuery query = new SolrQuery();
			query.set("q", queryText);
			query.setFilterQueries("contentType:Part");
			
			executeQuery(query);
		}
	}

	private String getPartIDFromPackageName(String packageName)
	{
		int i = packageName.indexOf("_");
		return packageName.substring(0, i);
	}
}
